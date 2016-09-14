package com.nihao001.sso.common.dto;

import java.io.Serializable;

public class ChangeJwtOutput implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private boolean success;

    private ChangeJwtOutput() {

    }

    public static final ChangeJwtOutput getInstant() {
        return new ChangeJwtOutput();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ChangeJwtOutput [success=" + success + "]";
    }
}
