package com.nihao001.sso.dao;

import org.apache.ibatis.annotations.Mapper;

import com.nihao001.sso.dao.model.SsoModel;
import com.nihao001.sso.dao.model.dto.SsoQueryDto;

@Mapper
public interface SsoModelMapper {
	int insert(SsoModel record);

	SsoModel selectByUsernameAndPlatform(SsoQueryDto ssoQueryDto);

	int updateByPrimaryKey(SsoModel record);
}