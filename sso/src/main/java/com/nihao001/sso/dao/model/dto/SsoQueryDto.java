package com.nihao001.sso.dao.model.dto;

import com.nihao001.sso.common.constant.Platform;

public class SsoQueryDto {

    private String username;
    private Platform platform;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

}
