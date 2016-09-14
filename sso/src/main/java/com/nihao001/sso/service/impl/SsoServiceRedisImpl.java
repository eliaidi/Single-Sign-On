package com.nihao001.sso.service.impl;

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nihao001.sso.common.constant.Platform;
import com.nihao001.sso.common.utils.Assert;
import com.nihao001.sso.dao.model.SsoModel;
import com.nihao001.sso.service.SsoService;

public class SsoServiceRedisImpl implements SsoService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public SsoModel getByUsernameAndPlatform(final String username, final Platform platform) {
        if (StringUtils.isBlank(username) || platform == null) {
            return null;
        }
        return redisTemplate.execute(new RedisCallback<SsoModel>() {
            @Override
            public SsoModel doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] data = connection.get(generateKey(username, platform));
                if (data == null) {
                    return null;
                }
                return JSON.parseObject(new String(data), SsoModel.class);
            }
        });
    }

    @Override
    public boolean addSsoModel(final SsoModel model) {
        if (model == null || StringUtils.isBlank(model.getUsername()) || model.getPlatform() == null
                || StringUtils.isBlank(model.getToken())) {
            return false;
        }
        model.setModifyTime(new Date());
        model.setCreateTime(new Date());
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = generateKey(model.getUsername(), model.getPlatform());
                byte[] data = connection.get(key);
                if (data != null) {
                    return Boolean.FALSE;
                }
                try{
                    connection.set(key, JSON.toJSONString(model).getBytes(Charset.forName("utf-8")));
                    return Boolean.TRUE;
                }
                catch(Exception e){
                    return Boolean.FALSE;
                }
                
            }
        });
    }

    @Override
    public boolean updateSsoModel(final SsoModel model) {
        if (model == null || StringUtils.isBlank(model.getUsername()) || model.getPlatform() == null
                || StringUtils.isBlank(model.getToken())) {
            return false;
        }
        model.setModifyTime(new Date());
        model.setCreateTime(new Date());
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = generateKey(model.getUsername(), model.getPlatform());
                byte[] data = connection.get(key);
                if (data == null) {
                    return Boolean.FALSE;
                }
                try{
                    connection.set(key, JSON.toJSONString(model).getBytes(Charset.forName("utf-8")));
                    return Boolean.TRUE;
                }
                catch(Exception e){
                    return Boolean.FALSE;
                }
                
            }
        });
    }

    private byte[] generateKey(String username, Platform platform) {
        Assert.notBlank(username, "the username can not be blank.");
        Assert.notNull(platform, "the platform can not be null.");
        return String.format("_sso.%s.%s", username, platform.name()).getBytes(Charset.forName("utf-8"));
    }

}
