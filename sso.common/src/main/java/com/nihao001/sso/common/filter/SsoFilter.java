package com.nihao001.sso.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nihao001.sso.common.Config;
import com.nihao001.sso.common.SsoClient;
import com.nihao001.sso.common.dto.JwtCheckOutput;
import com.nihao001.sso.common.dto.UserInfo;
import com.nihao001.sso.common.utils.Assert;
import com.nihao001.sso.common.utils.Utils;

public class SsoFilter implements Filter {
    
    private static final Log logger = LogFactory.getLog(SsoFilter.class);
    
    private static final List<Pattern> NOT_CHECK_URL_LIST = new ArrayList<Pattern>();
    
    
    private void setupUrlSet(String urlString, List<Pattern> patternList){
        if(!Utils.isStringBlank(urlString)){
            String[] urls = urlString.split(",");
            for(String url : urls){
                url = url.trim();
                if(Utils.isStringNotBlank(url)){
                    String patternStr = createPattern(url);
                    patternList.add(Pattern.compile(patternStr));
                }
            }
        }
    }
    
    private boolean checkUri(String uri){
        for(Pattern pattern : NOT_CHECK_URL_LIST){
            if(pattern.matcher(uri).matches()){
                return true;
            }
        }
        return false;
    }
    

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String urlString = filterConfig.getInitParameter("NOT_CHECK_URL_LIST");
        setupUrlSet(urlString, NOT_CHECK_URL_LIST);
    }
        

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest      = (HttpServletRequest)request;
        HttpServletResponse httpResponse    = (HttpServletResponse)response;
        
        String uri = httpRequest.getServletPath();
        if(checkUri(uri)){
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        
        String jwt = Utils.getJwt(httpRequest);
        // if there is no jwt found, jump to login page.
        if(Utils.isStringBlank(jwt)){
            logger.info("no jwt found, jump to login page.");
            httpResponse.sendRedirect(Utils.createRedirectUrl(Config.getInstant().getLoginPageUrl(), httpRequest.getRequestURL().toString(), null));
            return;
        }
        try {
            // check jwt
            JwtCheckOutput result = SsoClient.getInstant().check(jwt);
            if(!result.isSuccess()){
                logger.info("fail to check jwt["+result.getInfo().getExceptionInfo()+"],jump to login page.");
                httpResponse.sendRedirect(Utils.createRedirectUrl(Config.getInstant().getLoginPageUrl(), 
                        httpRequest.getRequestURL().toString(), result.getInfo().name()));
                return;
            }
            
            UserInfo userInfo = new UserInfo(result.getUserId(), result.getUsername());
            Utils.setUserInfoIntoRequest(httpRequest, userInfo);
            chain.doFilter(httpRequest, httpResponse);
        } catch (Exception e) {
            logger.error("some error occurs when check jwt.", e);
        }
    }

    @Override
    public void destroy() {
        
    }
    
    
    private static String createPattern(String pattern){
        int starIndex       = pattern.indexOf("*");
        int starStarIndex   = pattern.indexOf("**");
        Assert.assertTrue(pattern.indexOf("/") == 0, "must start with '/'.");
        Assert.assertTrue(!(starIndex >= 0 && starStarIndex >= 0 && starIndex != starStarIndex), "only one * or one ** in string pattern.");
        String newPattern = null;
        if(starIndex >= 0 && starStarIndex < 0){
            newPattern = "^" + pattern.replace("*", "[\\w\\.]+") + "$";
        }
        else if(starStarIndex >= 0){
            Assert.assertTrue(pattern.length() == starStarIndex + 2 , "after **, can not be any string.");
            newPattern = "^" + pattern.replace("*", "[\\w\\.\\/]+") + "$";
        }
        else{
            newPattern = "^" + pattern + "$";
        }
        return newPattern;
    }
    
    private static boolean test(String pattern, String str){
        String newPattern = createPattern(pattern);
        Pattern p = Pattern.compile(newPattern); 
        Matcher m = p.matcher(str);
        System.out.println(pattern + " =>" + str + "::" + m.find());
        return m.matches();
    }
    
    public static void main(String[] args) {
        
        
        String test1 = "/path1/*/path2";
        Assert.assertTrue(test(test1, "/path1/path2/path2"));
        Assert.assertTrue(test(test1, "/path1/ABCDE/path2"));
        Assert.assertTrue(test(test1, "/path1/ABC23423ddDE/path2"));
        Assert.assertFalse(test(test1, "/path1/ABC23423ddDE/path23434"));
        
        String test2 = "/path1/*";
        Assert.assertTrue(test(test2, "/path1/asdf33"));
        Assert.assertTrue(test(test2, "/path1/adee3"));
        Assert.assertTrue(test(test2, "/path1/zzxvsdf"));
        Assert.assertTrue(test(test2, "/path1/zAAAAdf33"));
        Assert.assertTrue(test(test2, "/path1/ZZZZZDSDF"));
        Assert.assertTrue(test(test2, "/path1/234234234.js"));
        Assert.assertTrue(test(test2, "/path1/234234234.css"));
        Assert.assertTrue(test(test2, "/path1/234234234.jpg"));
        
        String test3 = "/path1/*/path2/*/path3";
        Assert.assertTrue(test(test3, "/path1/asdfd/path2/34234/path3"));
        Assert.assertTrue(test(test3, "/path1/asdfd/path2/AAAAA/path3"));
        Assert.assertTrue(test(test3, "/path1/BBBBBBB/path2/AAAAA/path3"));
        Assert.assertTrue(test(test3, "/path1/BBBBBBB/path2/234234/path3"));
        Assert.assertFalse(test(test3, "/path1/BBBBBBB/path2/234234/path3aaa"));
        
        String test4 = "/path1/*/path2/*/*.js";
        Assert.assertTrue(test(test4, "/path1/asdf33/path2/asd3/dfdfe.js"));
        Assert.assertTrue(test(test4, "/path1/asdf33/path2/asd3/23434.js"));
        Assert.assertTrue(test(test4, "/path1/asdf33/path2/asd3/2sd3ad3.js"));
        Assert.assertTrue(test(test4, "/path1/asdf33/path2/Adfee/2sd3ad3.js"));
        Assert.assertFalse(test(test4, "/path1/asdf33/path2/Adfee/2sd3ad3.jsx"));
        
        String test5 = "/**";
        Assert.assertTrue(test(test5, "/asdf"));
        Assert.assertTrue(test(test5, "/asdf.html"));
        Assert.assertTrue(test(test5, "/asdf.jsp"));
        Assert.assertTrue(test(test5, "/asdf.git"));
        Assert.assertTrue(test(test5, "/asdf33/path2/asd3/dfdfe.js"));
        
        String test6 = "/path1/**";
        Assert.assertTrue(test(test6, "/path1/asdf"));
        Assert.assertTrue(test(test6, "/path1/asdf.html"));
        Assert.assertTrue(test(test6, "/path1/asdf.jsp"));
        Assert.assertTrue(test(test6, "/path1/asdf.git"));
        Assert.assertTrue(test(test6, "/path1/asdf33/path2/asd3/dfdfe.js"));
        
        String test7 = "/path1/dddd";
        Assert.assertTrue(test(test7, "/path1/dddd"));
        
        
     
    }


}
