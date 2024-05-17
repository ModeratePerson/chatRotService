package com.moderatePerson.controller;

import com.aliyuncs.exceptions.ClientException;
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

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
/**
 * 除登录路径(/login/**)外，其他请求都需要经过token校验才允许访问
 */
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
// 使用密码登录
    @PostMapping("/login/password")
    public ResponseEntity<?> login(String phoneNumber,String password){
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
//    Sms发送验证码
    @PostMapping("/login/sms")
    public ResponseEntity<?> sms(String phoneNumber){
        User user = userService.selectUserByPhoneNumber(phoneNumber);
//        用户已注册，直接发送验证码
        if (user!=null){
            int code = 100000 + new Random().nextInt(900000);
            String code1 = code + ""; //生成随机6位验证码
//            将验证码缓存在redis中有效期1分钟
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
    public ResponseEntity<?> code(String phoneNumber,String code){
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
}
