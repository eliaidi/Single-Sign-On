package com.nihao001.sso.common.constant;

public enum CheckResultEnum implements ExceptionType {

    TokenError("Check[-5]", "Token Error"),
    InnerError("Check[-4]", "Inner Error"),
    ExpireJwtError("Check[-3]", "Expire Jwt"),
    IllegalJwtError("Check[-2]", "Illeagal Jwt"),
    ParameterError("Login[-1]", "Parameter Error"),
    CheckSuccess("Check[0]", "Check Success");

    CheckResultEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    private String code;
    private String info;

    @Override
    public String getExceptionCode() {
        return code;
    }

    @Override
    public String getExceptionInfo() {
        return info;
    }
    
    

}
