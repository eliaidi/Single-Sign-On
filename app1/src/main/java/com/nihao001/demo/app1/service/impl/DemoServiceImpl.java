package com.nihao001.demo.app1.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nihao001.demo.app1.service.DemoService;
import com.nihao001.sso.common.SsoClient;
import com.nihao001.sso.common.constant.Platform;
import com.nihao001.sso.common.dto.LoginInput;
import com.nihao001.sso.common.dto.LoginOutput;
import com.nihao001.sso.common.service.AuthenticationService;

@Service
public class DemoServiceImpl implements DemoService {
    
    @Resource(name="AuthenticationService")
    private AuthenticationService authenticationService;
    

    @Override
    public String dubboTest() {
        LoginInput login = new LoginInput(); 
        login.setUsername("admin");
        login.setPassword("123456");
        login.setPlatform(Platform.WEB);
        login.setExpireTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        LoginOutput output = authenticationService.login(login);
        System.out.println(output);
        return output == null? "null" : output.toString();
    }

    @Override
    public String ssoClientTest() {
        LoginInput login = new LoginInput(); 
        login.setUsername("admin");
        login.setPassword("123456");
        login.setPlatform(Platform.WEB);
        login.setExpireTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        LoginOutput output = null;
        try {
            output = SsoClient.getInstant().login(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output == null? "null" : output.toString();
    }

}
