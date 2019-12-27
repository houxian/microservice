package com.imooc.user.response;

public class LonginResponse extends Response {

    private  String token;

    public LonginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
