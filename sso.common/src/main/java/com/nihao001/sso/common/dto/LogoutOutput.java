package com.nihao001.sso.common.dto;

import java.io.Serializable;

public class LogoutOutput implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private boolean success;

    private LogoutOutput() {

    }

    public static final LogoutOutput getInstant() {
        return new LogoutOutput();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "LogoutResult [success=" + success + "]";
    }

}
