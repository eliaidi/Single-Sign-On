package com.nihao001.sso.common.service;

import com.nihao001.sso.common.dto.ChangeJwtOutput;
import com.nihao001.sso.common.dto.JwtCheckOutput;
import com.nihao001.sso.common.dto.LoginInput;
import com.nihao001.sso.common.dto.LoginOutput;
import com.nihao001.sso.common.dto.LogoutOutput;

public interface AuthenticationService {
    
    /**
     * 登录
     * 
     * @param loginInput
     * @return
     */
    LoginOutput login(LoginInput loginInput);
    
    /**
     * 检验jwt
     * 
     * @param jwt
     * @return
     */
    JwtCheckOutput check(String jwt);
    
    /**
     * 注销
     * 
     * @param jwt
     * @return
     */
    LogoutOutput logout(String jwt);
    
    
    /**
     * 修改jwt(修改密码等其他业务会影响用户登录的业务)
     * 
     * @param jwt
     * @return
     */
    ChangeJwtOutput changeJwt(String jwt);

}


