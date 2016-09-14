package com.nihao001.sso.common.exception;

public class SsoException extends Exception {
    
    public SsoException(){
        super();
    }
    
    public SsoException(String message){
        super(message);
    }
    
    public SsoException(String message, Throwable cause){
        super(message, cause);
    }
    
    public SsoException(Throwable cause){
        super(cause);
    }
    

}
