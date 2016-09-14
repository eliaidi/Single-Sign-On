package com.nihao001.demo.app1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    
    @Value("${application.domain}")
    private String applicationDomain;
    
    @Value("${application.port}")
    private int applicationPort;
    
    
    
    public String getApplicationDomain(){
        return applicationDomain;
    }

    public int getApplicationPort() {
        return applicationPort;
    }
    
    
}
