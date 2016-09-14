package com.nihao001.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nihao001.sso.dao.UserModelMapper;
import com.nihao001.sso.dao.model.UserModel;
import com.nihao001.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserModelMapper userModelMapper;

	@Override
	public UserModel getUserByUsername(String username) {
		if(StringUtils.isBlank(username)){
			return null;
		}
		return userModelMapper.selectByUsername(username);
	}

}
