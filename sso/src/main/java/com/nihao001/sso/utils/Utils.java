package com.nihao001.sso.utils;

import java.util.UUID;

import org.apache.log4j.Logger;

public class Utils {
    private static  UUID uuid = UUID.randomUUID();
    
    private Utils(){
        
    }
    
    public static final String uuid(){
        return uuid.toString().replace("-", "");
    }

    
}
