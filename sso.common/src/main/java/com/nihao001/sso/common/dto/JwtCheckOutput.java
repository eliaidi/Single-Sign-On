package com.nihao001.sso.common.dto;

import java.io.Serializable;

import com.nihao001.sso.common.constant.CheckResultEnum;

public class JwtCheckOutput implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private boolean success = false;

    private CheckResultEnum info;

    private long userId;

    private String username;

    private JwtCheckOutput() {

    }

    public static final JwtCheckOutput getInstant() {
        return new JwtCheckOutput();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CheckResultEnum getInfo() {
        return info;
    }

    public void setInfo(CheckResultEnum info) {
        this.info = info;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "JwtCheckOutput [success=" + success + ", info=" + info + ", userId=" + userId + ", username=" + username
                + "]";
    }

}
