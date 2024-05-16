package com.moderatePerson.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

//用户
//使用lombok关联数据库表
@Data
public class User {
//    使用雪花算法自动生成uuid
        @TableId(type = IdType.ASSIGN_UUID)
        private String userId; // 用户ID
        private String username; // 用户名
        private String phoneNumber; // 手机号
        private String password; //首次注册用验证码，之后可选择使用密码登录
        private String avatarUrl; // 头像 URL
        private String levelId; // 用户等级ID，关联Level表
        private String itemId; // 用户购买套餐ID，关联Level表
        private String points; // 用户积分余额
        private Date registrationTime; // 注册时间
        private Date lastLoginTime; // 最后登录时间
        private String ipAddress; // 用户IP地址
        private Integer requestCount; // 1小时请求次数
    }
