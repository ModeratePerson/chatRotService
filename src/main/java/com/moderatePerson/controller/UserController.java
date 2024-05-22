package com.moderatePerson.controller;

import com.aliyuncs.exceptions.ClientException;
import com.moderatePerson.domain.VO.UserInfo;
import com.moderatePerson.domain.PO.User;
import com.moderatePerson.domain.pojo.Message;
import com.moderatePerson.service.UserService;
import com.moderatePerson.utils.jwt.JwtUtil;
import com.moderatePerson.utils.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /** 用户注册登录功能
    * 除含有/register/**,/login/**的路由均会被拦截，前端跳转到登录界面
    * 用户登录后会返回token,再次请求其他资源时必须在(前端在请求头中设置,{Authorization:"token内容"})request Header中携带token，经过token校验后才能访问
    * 请求时携带的token中包含用户手机号信息，即请求除登录注册外资源时前端无需再返回phoneNumber数据
    * 非登录注册controller方法中需要携带HttpServletRequest参数才能从token中解析得到phoneNumber
     */



    // 前端校验手机号格式，格式正确才传phoneNumber到后端
    @PostMapping("/register/sms")
    public ResponseEntity<?> registerSms(String phoneNumber){
        User user = userService.selectUserByPhoneNumber(phoneNumber);
        if (user!=null){
          // 该手机号已注册
            Message message = new Message();
            message.setMsg("该用户已存在");
            message.setStatus(409);
            // 前端直接返回到登录界面
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        int code = 100000 + new Random().nextInt(900000);
        String code1 = code + ""; //生成随机6位验证码
          // 将验证码缓存在redis中有效期1分钟
        redisTemplate.opsForValue().set(phoneNumber, code1, 1, TimeUnit.MINUTES);
        try {
            SmsService.sendCode(phoneNumber,code1);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        Message message = new Message();
        message.setMsg("发送成功");
        message.setStatus(200);
        return ResponseEntity.ok().body(message);
    }
    // 用户验证码注册，注册成功默认自动登录返回token
    @PostMapping("/register/code")
    public ResponseEntity<?> register(String phoneNumber,String code){
        // 从Redis中取出验证码
        String checkCode = redisTemplate.opsForValue().get(phoneNumber);
        // 比对验证码是否一致
        if (checkCode!=null&&checkCode.equals(code.toLowerCase())) {
            // 注册成功，删除Redis中的验证码
            redisTemplate.delete(phoneNumber);
            String username = "user_" + System.currentTimeMillis();// 根据系统时间生成默认用户名
            // 将用户插入数据库
            userService.insertUserByPhoneNumber(phoneNumber,username);
            User user = new User();
            user.setPhoneNumber(phoneNumber);
            String token = JwtUtil.createToken(user);//生成token
            Message message = new Message();
            message.setToken(token);
            message.setMsg("注册成功");
            message.setStatus(200);
            // 将用户信息返回到前端
            return ResponseEntity.ok().body(message);
        }
        Message message = new Message();
        message.setMsg("注册失败，验证码过期或错误");
        message.setStatus(401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
    // 设置密码，用户注册成功后直接跳转到设置密码界面,用户请求必须经过Token校验
    @PostMapping("/add/password")
    public ResponseEntity<?> addPassword(HttpServletRequest request,String password){
        String phoneNumber = JwtUtil.getPhoneNumber(request); //从token中获取phoneNumber
        userService.updatePasswordByPhoneNumber(phoneNumber, password);
        Message message = new Message();
        message.setStatus(200);
        message.setMsg("密码设置成功");
        return ResponseEntity.ok().body(message);
    }
 // 使用密码登录
    @PostMapping("/login/password")
    public ResponseEntity<?> loginByPassword(String phoneNumber,String password){
        User user = userService.selectUserByPassword(phoneNumber, password);
        if (user!=null){
            log.info("登录成功！生成token！");
            String token = JwtUtil.createToken(user);
            Message message = new Message();
            message.setToken(token);
            message.setMsg("登录成功");
            message.setStatus(200);
/*返回JWT令牌作为响应主体内容，前端需要将token保存在浏览器中,
发起请求时需要在RequestHeader中添加Authorization:“token”*/
            return ResponseEntity.ok().body(message);
        }
        else {
            Message message = new Message();
            message.setMsg("登录失败，用户名或密码错误");
            message.setStatus(401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
    }
   // Sms发送验证码
    @PostMapping("/login/sms")
    public ResponseEntity<?> loginSms(String phoneNumber){
        User user = userService.selectUserByPhoneNumber(phoneNumber);
        // 用户已注册，直接发送验证码
        if (user!=null){
            int code = 100000 + new Random().nextInt(900000);
            String code1 = code + ""; //生成随机6位验证码
           // 将验证码缓存在redis中有效期1分钟
            redisTemplate.opsForValue().set(phoneNumber, code1, 1, TimeUnit.MINUTES);
            try {
                SmsService.sendCode(phoneNumber,code1);
            } catch (ClientException e) {
                throw new RuntimeException(e);
            }
            Message message = new Message();
            message.setMsg("发送成功");
            message.setStatus(200);
            return ResponseEntity.ok().body(message);
        }
        //用户未注册，返回错误信息跳转到登录界面
        Message message = new Message();
        message.setStatus(401);
        message.setMsg("您还没有注册，即将跳转到注册界面");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
    //使用验证码登录,使用redis缓存校验60s内有效
    @PostMapping("/login/code")
    public ResponseEntity<?> loginByCode(String phoneNumber,String code){
        // 从Redis中取出验证码
        String checkCode = redisTemplate.opsForValue().get(phoneNumber);
        // 比对验证码是否一致
        if (checkCode!=null&&checkCode.equals(code.toLowerCase())) {

            // 登录成功，删除Redis中的验证码
            redisTemplate.delete(phoneNumber);
            User user = new User();
            user.setPhoneNumber(phoneNumber);
            String token = JwtUtil.createToken(user);//生成token
            Message message = new Message();
            message.setToken(token);
            message.setMsg("登录成功");
            message.setStatus(200);
            // 将用户信息返回到前端
            return ResponseEntity.ok().body(message);
        }
        Message message = new Message();
        message.setMsg("登录失败，验证码过期或错误");
        message.setStatus(401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
    /**
    * 用户个人信息界面，包含用户名(默认生成,可修改)，修改密码的接口，当前使用套餐类型，账户余额(积分形式)，用户等级(可以享受优惠)
     */

    // 查询用户基本信息
    @GetMapping("/info")
    public ResponseEntity<?> info(HttpServletRequest request){
        String phoneNumber = JwtUtil.getPhoneNumber(request);
        UserInfo userInfo = userService.selectUserInfo(phoneNumber);
        Message message = new Message();
        message.setStatus(200);
        message.setData(userInfo);
        return ResponseEntity.ok().body(message);
    }
    // 修改密码信息
    @PostMapping("/update/password")
    public ResponseEntity<?> updatePassword(HttpServletRequest request,String password){
        String phoneNumber = JwtUtil.getPhoneNumber(request); //从token中获取phoneNumber
        userService.updatePasswordByPhoneNumber(phoneNumber, password);
        Message message = new Message();
        message.setStatus(200);
        message.setMsg("密码修改成功");
        return ResponseEntity.ok().body(message);
    }
    // 用户名修改
    @PostMapping("/update/username")
    public ResponseEntity<?> updateUserName(HttpServletRequest request,String username){
        String phoneNumber = JwtUtil.getPhoneNumber(request); //从token中获取phoneNumber
        userService.updateUserNameByPhoneNumber(phoneNumber);
        Message message = new Message();
        message.setStatus(200);
        message.setMsg("用户名修改成功");
        return ResponseEntity.ok().body(message);
    }

}
