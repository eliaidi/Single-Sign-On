package com.nihao001.sso.common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.nihao001.sso.common.constant.Platform;
import com.nihao001.sso.common.dto.JwtCheckOutput;
import com.nihao001.sso.common.dto.LoginInput;
import com.nihao001.sso.common.dto.LoginOutput;
import com.nihao001.sso.common.dto.LogoutOutput;
import com.nihao001.sso.common.utils.Utils;

/**
 * SSO client
 * 
 * @author maomao
 *
 */
public final class SsoClient {
    private static final Log logger = LogFactory.getLog(SsoClient.class);
    
    private static final String LOGIN   = "/login";
    private static final String CHECK   = "/check";
    private static final String LOGOUT  = "/logout";
    
    private CloseableHttpAsyncClient httpclient;
    
    private static SsoClient ssoClient;
    
    private SsoClient() {
        logger.info("SsoClient Init.");
        httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
    }
    
    public static synchronized final SsoClient getInstant(){
        if(ssoClient == null){
            ssoClient = new SsoClient();
        }
        return ssoClient;
    }
    
    public synchronized void close(){
        if(httpclient.isRunning()){
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("close httpcleint error.", e);
            }
        }
        ssoClient = null;
    }

    public final LoginOutput login(LoginInput loginInput) throws Exception{
        if(loginInput == null || Utils.isStringBlank(loginInput.getUsername()) 
                || Utils.isStringBlank(loginInput.getPassword()) 
                || loginInput.getPlatform() == null
                || loginInput.getExpireTime() <= 0){
            throw new IllegalArgumentException("username, password, platfrom can not be null.And expireTime must be more than 0.");
        }
        final HttpPost request = new HttpPost(Config.getInstant().getSsoServer() + LOGIN);
        // set json as http request body.
        StringEntity postBody = new StringEntity(JSON.toJSONString(loginInput));
        postBody.setContentEncoding("UTF-8");    
        postBody.setContentType("application/json");
        request.setEntity(postBody);
        
        Future<HttpResponse> future = httpclient.execute(request, null);
        HttpResponse response = future.get();
        if(response.getStatusLine().getStatusCode() == 200){
            String text = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            return JSON.parseObject(text, LoginOutput.class);
        }
        return null;
    }

    public final JwtCheckOutput check(String token) throws Exception {
        if(Utils.isStringBlank(token)){
            throw new IllegalArgumentException("the token is blank when check.");
        }
        final HttpGet request = new HttpGet(Config.getInstant().getSsoServer() + CHECK);
        request.addHeader("jwt", token);
        Future<HttpResponse> future = httpclient.execute(request, null);
        HttpResponse response = future.get();
        if(response.getStatusLine().getStatusCode() == 200){
            String text = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            return JSON.parseObject(text, JwtCheckOutput.class);
        }
        return null;
    }

    public final LogoutOutput logout(String token) throws Exception {
        if(Utils.isStringBlank(token)){
            throw new IllegalArgumentException("the token is blank when logout.");
        }
        final HttpGet request = new HttpGet(Config.getInstant().getSsoServer() + LOGOUT);
        request.addHeader("jwt", token);
        Future<HttpResponse> future = httpclient.execute(request, null);
        HttpResponse response = future.get();
        if(response.getStatusLine().getStatusCode() == 200){
            String text = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            return JSON.parseObject(text, LogoutOutput.class);
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        LoginInput loginInput = new LoginInput();
        loginInput.setUsername("admin");
        loginInput.setPassword("123456");
        loginInput.setPlatform(Platform.WEB);
        loginInput.setExpireTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        SsoClient ssoClient = null;
        try{
            ssoClient = SsoClient.getInstant();
            LoginOutput loginOutput = ssoClient.login(loginInput);
            System.out.println(loginOutput);
            
            JwtCheckOutput checkOutput = ssoClient.check(loginOutput.getToken());
            System.out.println(checkOutput);
            
            LogoutOutput logoutResult = ssoClient.logout(loginOutput.getToken());
            System.out.println(logoutResult);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            ssoClient.close();
        }

    }

}
