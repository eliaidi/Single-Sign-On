package com.nihao001.sso.common.dto;

public class UserInfo {
    
    public UserInfo(long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    private long userId;
    private String username;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    
}
