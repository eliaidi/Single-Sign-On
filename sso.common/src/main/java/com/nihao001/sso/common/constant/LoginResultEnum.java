package com.nihao001.sso.common.constant;

public enum LoginResultEnum implements ExceptionType {

    UserNotExistError("Login[-1]", "User Not Exist"),
    PasswordError("Login[-2]", "Password Error"),
    ParameterError("Login[-3]", "Parameter Error"),
    InnerError("Login[-4]", "Inner Error"),
    LoginSuccess("Login[0]", "Login Success");

    LoginResultEnum(String code, String info) {
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
