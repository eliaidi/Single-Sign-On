package com.nihao001.sso.service.dto;

import com.nihao001.sso.common.constant.Platform;

/**
 * This bean is used for converting JWT token after loginning successfully;
 * 
 * @author maomao
 *
 */
public class LoginedUserInfo {
    private long userId;
    private String username;
    private long expireTime;
    private String innerToken;
    private Platform platform;

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

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getInnerToken() {
        return innerToken;
    }

    public void setInnerToken(String innerToken) {
        this.innerToken = innerToken;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    

}
