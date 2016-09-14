package com.nihao001.sso.config;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    private static final Logger logger = Logger.getLogger(AppConfig.class);
    
    @Value("${application.jwt.key}")
    private String jwtKey;
    
    @Value("${application.root.domain}")
    private String rootDomain;
    
    @Value("${application.default.redirect.url}")
    private String defaultRedirectUrl;

    /**
     * Generate a key which is used for jwt encoding.
     * 
     * @return
     */
    @Bean(name = "jwtKey")
    public SecretKey getJwtKey() {
        ByteArrayOutputStream out = new ByteArrayOutputStream(32);
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            byte[] keyStrBytes = this.jwtKey.getBytes("utf-8");
            byte[] md51 = md.digest(keyStrBytes);
            out.write(md51);
            byte[] temp2 = new byte[keyStrBytes.length + md51.length];
            System.arraycopy(md51, 0, temp2, 0, 16);
            System.arraycopy(keyStrBytes, 0, temp2, 16, keyStrBytes.length);
            byte[] md52 = md.digest(temp2);
            out.write(md52);
        } catch (Exception e) {
            logger.error("generate jwt key error.", e);
        }
        return new SecretKeySpec(out.toByteArray(), "HmacSHA256");
    }
    
    @Bean(name = "rootDomain")
    public String getRootDomain(){
        return this.rootDomain;
    }

    @Bean(name = "defaultRedirectUrl")
    public String getDefaultRedirectUrl() {
        return defaultRedirectUrl;
    }
    
    
}
