package com.nihao001.sso.common.dto;

import java.io.Serializable;

import com.nihao001.sso.common.constant.LoginResultEnum;

public class LoginOutput implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private boolean loginSuccess = false;

    private LoginResultEnum info;

    private String token;

    public static final LoginOutput getInstant() {
        return new LoginOutput();
    }

    private LoginOutput() {

    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResultEnum getInfo() {
        return info;
    }

    public void setInfo(LoginResultEnum info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "LoginOutput [loginSuccess=" + loginSuccess + ", info=" + info + ", token=" + token + "]";
    }

}
