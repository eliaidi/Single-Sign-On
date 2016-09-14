package com.nihao001.sso.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nihao001.sso.common.constant.Platform;
import com.nihao001.sso.dao.SsoModelMapper;
import com.nihao001.sso.dao.model.SsoModel;
import com.nihao001.sso.dao.model.dto.SsoQueryDto;
import com.nihao001.sso.service.SsoService;

@Service
public class SsoServiceMysqlImpl implements SsoService {
    
    @Autowired
    private SsoModelMapper ssoModelMapper;

    @Override
    public SsoModel getByUsernameAndPlatform(String username, Platform platform) {
        if(StringUtils.isBlank(username) || platform == null){
            return null;
        }
        SsoQueryDto query = new SsoQueryDto();
        query.setUsername(username);
        query.setPlatform(platform);
        return ssoModelMapper.selectByUsernameAndPlatform(query);
    }


    @Override
    public boolean addSsoModel(SsoModel model) {
        if(model == null || StringUtils.isBlank(model.getUsername())
            || model.getPlatform() == null
            || StringUtils.isBlank(model.getToken())){
            return false;
        }
        if(getByUsernameAndPlatform(model.getUsername(), model.getPlatform()) != null){
            return false;
        }
        model.setModifyTime(new Date());
        model.setCreateTime(new Date());
        return ssoModelMapper.insert(model) > 0;
    }

    @Override
    public boolean updateSsoModel(SsoModel model) {
        if(model == null || StringUtils.isBlank(model.getUsername())
                || model.getPlatform() == null
                || StringUtils.isBlank(model.getToken())
                || model.getId() <= 0){
                return false;
        }
        model.setModifyTime(new Date());
        return ssoModelMapper.updateByPrimaryKey(model) > 0;
    }

}
