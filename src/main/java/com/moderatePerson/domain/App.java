package com.moderatePerson.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//用户使用模型关联的场景app,如微信，抖音
//使用lombok关联数据库表
@Data
public class App {
    @TableId
    private String appId;
    private String appName;
//    应用隐私链接
    private String privacyPolicyLink;
//    可以实现的功能，如信息搜集，自动评论
    private String function;
}
