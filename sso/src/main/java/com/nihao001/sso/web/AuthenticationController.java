package com.nihao001.sso.web;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nihao001.sso.common.constant.Platform;
import com.nihao001.sso.common.dto.LoginInput;
import com.nihao001.sso.common.dto.LoginOutput;
import com.nihao001.sso.common.service.AuthenticationService;




@Controller
public class AuthenticationController {
    private static final Logger logger = Logger.getLogger(AuthenticationController.class);
    
    @Resource(name="AuthenticationService")
    private AuthenticationService authenticationService;
    
    @Resource(name="rootDomain")
    private String rootDomain;
    
    @Resource(name="defaultRedirectUrl")
    private String defaultRedirectUrl;
	
	
    @RequestMapping(method = RequestMethod.POST, value="/login")
    @ResponseBody
    public Object login(@RequestBody LoginInput loginInput){
        LoginOutput result =  authenticationService.login(loginInput);
        return result;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/loginAndJump")
    public ModelAndView loginAndjump(HttpServletRequest request, HttpServletResponse response, LoginInput loginInput){
        String url = request.getParameter("redirectURL");
        ModelAndView mv = new ModelAndView();
        if(StringUtils.isBlank(url)){
            url = defaultRedirectUrl;
        }
        loginInput.setPlatform(Platform.WEB);
        LoginOutput result =  authenticationService.login(loginInput);
        // login success and jump to another url.
        if(result.isLoginSuccess()){
            try {
                Cookie cookie = new Cookie("jwt", result.getToken());
                cookie.setDomain(rootDomain);
                // 30 days
                cookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cookie);
                mv.setViewName("redirect:" + url);
                return mv;
            } catch (Exception e) {
                logger.error("jump to another url error. target url is:\r\n" + url, e);
                mv.addObject("info", "Unknow Error");
                mv.setViewName("login");
                return mv;
            }
        }
        else{
            mv.setViewName("login");
            mv.addObject("info", result.getInfo().getExceptionInfo());
            return mv;
        }
    }
    
    @RequestMapping("/check")
    @ResponseBody
    public Object check(HttpServletRequest request){
        return authenticationService.check(getJwt(request));
    }
    
    @RequestMapping("/logout")
    @ResponseBody
    public Object logout(HttpServletRequest request){
        return authenticationService.logout(getJwt(request));
    }
    
    @RequestMapping("/")
    @ResponseBody
    public String home(){
        return "Hello!";
    }
    
    @GetMapping("/sso_login")
    public String sso(HttpServletRequest request, ModelMap modelMap){
        String app = request.getParameter("app");
        modelMap.addAttribute("app", app);
        return "login";
    }
    
    private String getJwt(HttpServletRequest request){
        String jwt = request.getParameter("jwt");
        if(StringUtils.isBlank(jwt)){
            jwt = request.getHeader("jwt");
        }
        return jwt;
    }
	
}
