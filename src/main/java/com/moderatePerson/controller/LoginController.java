package com.moderatePerson.controller;

import com.moderatePerson.domain.User;
import com.moderatePerson.utils.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录Controller
 */
@Slf4j
@RestController
public class LoginController
{
    static Map<Integer, User> userMap = new HashMap<>();

    static {
        //模拟数据库
        User user1 = new User();
        user1.setUserId("1");
        user1.setUsername("zyf");
        user1.setPassword("132676");
        userMap.put(1, user1);
        User user2 = new User();
        user2.setUserId("2");
        user2.setUsername("zyz");
        user2.setPassword("132624");
        userMap.put(2, user2);
    }

    /**
     * 模拟用户 登录
     */
    @RequestMapping("/login")
    public String login(User user)
    {
        for (User dbUser : userMap.values()) {
            if (dbUser.getUsername().equals(user.getUsername()) && dbUser.getPassword().equals(user.getPassword())) {
                log.info("登录成功！生成token！");
                String token = JwtUtil.createToken(dbUser);
                return token;
            }
        }
        return "";
    }
}
