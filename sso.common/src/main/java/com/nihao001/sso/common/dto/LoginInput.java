package com.nihao001.sso.common.dto;

import java.io.Serializable;

import com.nihao001.sso.common.constant.Platform;

/**
 * login info
 * 
 * @author maomao
 *
 */
public class LoginInput implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private Platform platform;
    // milliseconds
    private long expireTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "LoginInput [username=" + username + ", password=" + password + ", platform=" + platform
                + ", expireTime=" + expireTime + "]";
    }

}
