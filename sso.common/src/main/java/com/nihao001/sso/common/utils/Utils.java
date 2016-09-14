package com.nihao001.sso.common.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nihao001.sso.common.constant.Constant;
import com.nihao001.sso.common.dto.UserInfo;

public class Utils {
    private static final Log logger = LogFactory.getLog(Utils.class);

    public static boolean isStringBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isStringNotBlank(final CharSequence cs) {
        return !isStringBlank(cs);
    }
    
    

    /**
     * get jwt from request(parameter, header or cookie)
     * 
     * 
     * @param request
     * @return
     */
    public static String getJwt(HttpServletRequest request) {
        String jwt = request.getParameter(Constant.SSO_KEY);
        if (isStringBlank(jwt)) {
            jwt = request.getHeader(Constant.SSO_KEY);
        }
        if(isStringBlank(jwt)){
            jwt = request.getHeader(Constant.SSO_STANDARD_KEY);
            if(!isStringBlank(jwt)){
                int index = jwt.indexOf("Bearer ");
                if(index >= 0){
                    jwt = jwt.substring(index + "Bearer ".length());
                }
            }
        }
        if (isStringBlank(jwt)) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies) {
                    if (Constant.SSO_KEY.equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }   
            }
        }
        return jwt;
    }

    public static final String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            logger.error("encode url error.url:\r\n" + url, e);
        }
        return null;
    }

    public static final String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (Exception e) {
            logger.error("decode url error.url:\r\n" + url, e);
        }
        return null;
    }
    
    /**
     * create login url with a url which is used for redirect after loginning.
     * 
     * @param loginUrl
     * @param targetUrl
     * @return
     */
    public static final String createRedirectUrl(String loginUrl, String targetUrl, String reason){
        String checkFail = "";
        if(!isStringBlank(reason)){
            checkFail = "&checkFail=" + reason;
        }
        if(loginUrl.indexOf("?") >= 0){
            return String.format(loginUrl + "&" + Constant.REDIRECT_URL_PARAM_NAME + "=%s", encodeUrl(targetUrl) + checkFail);
        }
        else{
            return String.format(loginUrl + "?" + Constant.REDIRECT_URL_PARAM_NAME + "=%s", encodeUrl(targetUrl) + checkFail);
        }
    }
    
    public static final void setUserInfoIntoRequest(HttpServletRequest request, UserInfo user){
        request.setAttribute(Constant.SESSION_USER_KEY, user);
    }
    
    public static final UserInfo getUserInfoFromRequest(HttpServletRequest request){
        return (UserInfo)request.getAttribute(Constant.SESSION_USER_KEY);
    }
    
}
