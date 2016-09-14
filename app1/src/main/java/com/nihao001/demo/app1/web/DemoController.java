package com.nihao001.demo.app1.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nihao001.demo.app1.ApplicationConfig;
import com.nihao001.demo.app1.service.DemoService;

@Controller
public class DemoController {
    
    @Autowired
    private DemoService demoService;
    
    @Autowired
    private ApplicationConfig applicationConfig;
    
    @RequestMapping("/dubboTest")
    @ResponseBody
    public String dubboTest(){
        return demoService.dubboTest();
    }
    
    @RequestMapping("/ssoClientTest")
    @ResponseBody
    public String ssoClientTest(){
        return demoService.ssoClientTest();
    }
    
    @RequestMapping("/checkJwt")
    public String checkJwt(){
        return "checkjwt";
    }
    
    @RequestMapping("/notCheckJwt")
    public String notCheckJwt(){
        return "notcheckjwt";
    }
    
    
    @RequestMapping("/")
    public String index(ModelMap modelMap){
        modelMap.put("applicationConfig", applicationConfig);
        return "index";
    }
}
