package com.nihao001.demo.app1;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nihao001.sso.common.filter.SsoFilter;

/**
 *  Web sso filter. 
 *
 * 
 * @author maomao
 *
 */
@Configuration
public class FilterConfig {
    
    @Value("${application.sso.not_check_url}")
    private String notCheckUrlString;

    @Bean
    public FilterRegistrationBean shallowEtagHeaderFilter() {
        FilterRegistrationBean registration= new FilterRegistrationBean(){
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException{
                SsoFilter ssoFilter = new SsoFilter();
                FilterRegistration.Dynamic filter = servletContext.addFilter("ssoFilter", ssoFilter);
                filter.setInitParameter("NOT_CHECK_URL_LIST", notCheckUrlString);
                filter.addMappingForUrlPatterns(null, true, "/*");
                //super.onStartup(servletContext);
            }
        };
        return registration;
    }
}
