package com.nihao001.sso.dao.model;

import java.io.Serializable;
import java.util.Date;

import com.nihao001.sso.common.constant.Platform;


public class SsoModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private Platform platform;

    private Date createTime;

    private Date modifyTime;

    private String token;
    
    
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
}