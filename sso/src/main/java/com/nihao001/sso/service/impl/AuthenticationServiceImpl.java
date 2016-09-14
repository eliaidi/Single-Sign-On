package com.nihao001.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nihao001.sso.common.constant.CheckResultEnum;
import com.nihao001.sso.common.constant.LoginResultEnum;
import com.nihao001.sso.common.dto.ChangeJwtOutput;
import com.nihao001.sso.common.dto.JwtCheckOutput;
import com.nihao001.sso.common.dto.LoginInput;
import com.nihao001.sso.common.dto.LoginOutput;
import com.nihao001.sso.common.dto.LogoutOutput;
import com.nihao001.sso.common.service.AuthenticationService;
import com.nihao001.sso.dao.model.SsoModel;
import com.nihao001.sso.dao.model.UserModel;
import com.nihao001.sso.service.JwtService;
import com.nihao001.sso.service.SsoService;
import com.nihao001.sso.service.UserService;
import com.nihao001.sso.service.dto.LoginedUserInfo;
import com.nihao001.sso.utils.Utils;

@Service(value="AuthenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private static final Logger logger = Logger.getLogger(AuthenticationServiceImpl.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private SsoService ssoService;
	@Autowired
	private JwtService jwtService;

	@Override
	public LoginOutput login(LoginInput loginInput) {
		LoginOutput result = LoginOutput.getInstant();
		if(StringUtils.isBlank(loginInput.getUsername()) 
		        || StringUtils.isBlank(loginInput.getPassword()) 
		        || loginInput.getPlatform() == null){
		    result.setInfo(LoginResultEnum.ParameterError);
            return result;
		}
		if(loginInput.getExpireTime() <= 0){
		    loginInput.setExpireTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
		}
		// check password
		UserModel user = userService.getUserByUsername(loginInput.getUsername());
		if(user == null){
			result.setInfo(LoginResultEnum.UserNotExistError);
			return result;
		}
		if(!loginInput.getPassword().equals(user.getPassword())){
		    result.setInfo(LoginResultEnum.PasswordError);
            return result;
		}
		
		// process sso info
		SsoModel sso = ssoService.getByUsernameAndPlatform(loginInput.getUsername(), loginInput.getPlatform());
		if(sso == null){
		    sso = new SsoModel();
		    sso.setUsername(loginInput.getUsername());
		    sso.setPlatform(loginInput.getPlatform());
		    sso.setToken(Utils.uuid());
		    if(!ssoService.addSsoModel(sso)){
		        logger.error("insert new sso model error.username:" + loginInput.getUsername());
		        result.setInfo(LoginResultEnum.InnerError);
	            return result;
		    }
		}
		else{
		    sso.setUsername(loginInput.getUsername());
		    sso.setPlatform(loginInput.getPlatform());
            sso.setToken(Utils.uuid());
            if(!ssoService.updateSsoModel(sso)){
                logger.error("update sso model error.username:" + loginInput.getUsername());
                result.setInfo(LoginResultEnum.InnerError);
                return result;
            }
		}
		
		// process jwt
		LoginedUserInfo loginedInfo = new LoginedUserInfo();
		loginedInfo.setPlatform(loginInput.getPlatform());
		loginedInfo.setExpireTime(loginInput.getExpireTime());
		loginedInfo.setInnerToken(sso.getToken());
		loginedInfo.setUserId(user.getId());
		loginedInfo.setUsername(sso.getUsername());
		
		result.setToken(jwtService.encode(loginedInfo));
		result.setLoginSuccess(true);
		result.setInfo(LoginResultEnum.LoginSuccess);
        return result;
	}

    @Override
    public JwtCheckOutput check(String jwt) {
        JwtCheckOutput result = JwtCheckOutput.getInstant();
        LoginedUserInfo loginedUserInfo = jwtService.decode(jwt);
        if(loginedUserInfo == null){
            result.setInfo(CheckResultEnum.IllegalJwtError);
            return result;
        }
        // check expire time
        if(System.currentTimeMillis() >= loginedUserInfo.getExpireTime()){
            result.setInfo(CheckResultEnum.ExpireJwtError);
            return result;
        }
        SsoModel sso = ssoService.getByUsernameAndPlatform(loginedUserInfo.getUsername(), loginedUserInfo.getPlatform());
        if(sso ==  null){
            logger.error("can not find sso info from storage");
            result.setInfo(CheckResultEnum.InnerError);
            return result;
        }
        // compare token.
        if(!loginedUserInfo.getInnerToken().equals(sso.getToken())){
            result.setInfo(CheckResultEnum.TokenError);
            return result;
        }
        result.setSuccess(true);
        result.setUserId(loginedUserInfo.getUserId());
        result.setUsername(loginedUserInfo.getUsername());
        return result;
    }

    @Override
    public LogoutOutput logout(String jwt) {
        LogoutOutput result = LogoutOutput.getInstant();
        // logout successfully.
        result.setSuccess(changeJwt(jwt).isSuccess());
        return result;
    }
    
    

    @Override
    public ChangeJwtOutput changeJwt(String jwt) {
        ChangeJwtOutput result = ChangeJwtOutput.getInstant();
        LoginedUserInfo loginedUserInfo = jwtService.decode(jwt);
        if(loginedUserInfo == null){
            logger.error("loginedUserInfo from jwt is null.");
            return result;
        }
        SsoModel sso = ssoService.getByUsernameAndPlatform(loginedUserInfo.getUsername(), loginedUserInfo.getPlatform());
        if(sso == null){
            logger.error("can not find sso info from storage");
            return result;
        }
        if(!loginedUserInfo.getInnerToken().equals(sso.getToken())){
            logger.error("the jwt inner token can not equal the token from storage.");
            return result;
        }
        sso.setToken(Utils.uuid());
        if(!ssoService.updateSsoModel(sso)){
            logger.error("update sso info error.");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

}
