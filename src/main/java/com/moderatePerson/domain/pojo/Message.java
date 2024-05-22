package com.moderatePerson.domain.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    // jwtToken,用于判断用户登录状态
    public String token;
    // 返回的文字信息
    public String msg;
    // 响应体数据部分
    public Object data;
    // 响应码
    public Integer status;
}
