package com.moderatePerson.domain.pojo;

public class Message {
//    jwtToken,用于判断用户登录状态
    public String token;
//    返回的文字信息
    public String msg;
    //    响应体数据部分
    public Object data;
//    响应码
    public Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
