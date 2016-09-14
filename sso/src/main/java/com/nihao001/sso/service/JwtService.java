package com.nihao001.sso.service;

import com.nihao001.sso.service.dto.LoginedUserInfo;

public interface JwtService {
    
    String encode(LoginedUserInfo loginedUserInfo);
    
    LoginedUserInfo decode(String jwt);
}
