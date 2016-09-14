package com.nihao001.sso.dao;

import org.apache.ibatis.annotations.Mapper;

import com.nihao001.sso.dao.model.UserModel;


@Mapper
public interface UserModelMapper {
	
	UserModel selectByUsername(String username);

}