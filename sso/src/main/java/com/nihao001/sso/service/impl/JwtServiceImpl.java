package com.nihao001.sso.service.impl;

import javax.annotation.Resource;
import javax.crypto.SecretKey;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nihao001.sso.service.JwtService;
import com.nihao001.sso.service.dto.LoginedUserInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {
    private static final Logger logger = Logger.getLogger(JwtServiceImpl.class);
    
    @Resource
    @Qualifier("jwtKey")
    private SecretKey jwtKey;

    @Override
    public String encode(LoginedUserInfo loginedUserInfo) {
        if(loginedUserInfo == null){
            logger.error("loginedUserInfo is null.");
            return null;
        }
        try{
            return Jwts.builder()
                    .setPayload(JSON.toJSONString(loginedUserInfo))
                    .signWith(SignatureAlgorithm.HS256, this.jwtKey)
                    //.compressWith(CompressionCodecs.DEFLATE)
                    .compact();
        }
        catch(Exception e){
            logger.error("encode loginedUserInfo to jwt error.loginedUserInfo:\r\n" + loginedUserInfo, e);
            return null;
        }
    }

    @Override
    public LoginedUserInfo decode(String jwt) {
        if(StringUtils.isBlank(jwt)){
            logger.error("jwt is blank.");
            return null;
        }
        try{
            Object claims = Jwts.parser().setSigningKey(this.jwtKey).parse(jwt).getBody();
            return JSON.parseObject(JSON.toJSONString(claims), LoginedUserInfo.class);
        }
        catch(Exception e){
            logger.error("decode jwt error.jwt:\r\n" + jwt, e);
            return null;
        }
    }
    
    /*
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        LoginedUserInfo loginedUserInfo = new LoginedUserInfo();
        loginedUserInfo.setApp("qb");
        // 30 days
        loginedUserInfo.setExpireTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30);
        loginedUserInfo.setUserId(2831723L);
        loginedUserInfo.setUsername("xingyue.wang@sumscope.com");
        loginedUserInfo.setInnerToken(IdUtils.uuid());
        
        System.out.println(loginedUserInfo);
        
        MessageDigest md    = MessageDigest.getInstance("MD5");
        String keyStr       = "123456";
        byte[] keyStrBytes  = keyStr.getBytes("utf-8");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(32);
        byte[] md51 = md.digest(keyStrBytes);
        out.write(md51);
        
        byte[] temp2 = new byte[keyStrBytes.length + md51.length];
        System.arraycopy(md51, 0, temp2, 0, 16);
        System.arraycopy(keyStrBytes, 0, temp2, 16, keyStrBytes.length);
        
        byte[] md52 = md.digest(temp2);
        out.write(md52);
       
        SecretKeySpec keySpec =  new SecretKeySpec(out.toByteArray(), "HmacSHA256");
        System.out.println(keySpec);
        
        Key key = MacProvider.generateKey();

        String compactJws1 = Jwts.builder()
          .setPayload(JSON.toJSONString(loginedUserInfo))
          .signWith(SignatureAlgorithm.HS256, keySpec)
          //.compressWith(CompressionCodecs.DEFLATE)
          .compact();
        
        System.out.println(compactJws1);
        
        Object claims = Jwts.parser().setSigningKey(keySpec).parse(compactJws1).getBody();
        LoginedUserInfo info = JSON.parseObject(JSON.toJSONString(claims), LoginedUserInfo.class);
        System.out.println(info);
    }
    */



    

}

