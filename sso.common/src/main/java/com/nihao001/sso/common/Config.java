package com.nihao001.sso.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configuration
 * 
 * @author maomao
 *
 */
public class Config {
    private static final Log logger = LogFactory.getLog(Config.class);
    
    private static Config config = null; 
    
    private String ssoServer;
    private String loginPageUrl;
    
    private Config(){
        InputStream input = null;
        try{
            input = Config.class.getClassLoader().getResourceAsStream("sso.properties");
            Properties properties = new Properties();
            properties.load(input);
            this.ssoServer      = properties.getProperty("sso.server");
            this.loginPageUrl   = properties.getProperty("sso.login.url");
        }
        catch(Exception e){
            logger.error("load sso config error.", e);
        }
        finally{
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("close resource inputstream error.", e);
                }
            }
        }
    }
    
    public synchronized static final Config getInstant(){
        if(config == null){
            config = new Config();
        }
        return config;
    }

    public String getSsoServer() {
        return ssoServer;
    }

    public String getLoginPageUrl() {
        return loginPageUrl;
    }
}
