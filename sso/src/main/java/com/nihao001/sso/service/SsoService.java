package com.nihao001.sso.service;

import com.nihao001.sso.common.constant.Platform;
import com.nihao001.sso.dao.model.SsoModel;

public interface SsoService {
    
    public SsoModel getByUsernameAndPlatform(String username, Platform platform);
    
    public boolean addSsoModel(SsoModel model);
    
    public boolean updateSsoModel(SsoModel model);
    
}
