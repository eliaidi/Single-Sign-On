package com.nihao001.sso.service;

import com.nihao001.sso.dao.model.UserModel;

public interface UserService {
	
	/**
	 * Query valid user by username;
	 * 
	 * @param username
	 * @return
	 */
	UserModel getUserByUsername(String username);
}
