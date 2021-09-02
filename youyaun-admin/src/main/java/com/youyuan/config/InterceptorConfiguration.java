package com.youyuan.config;

import com.youyuan.interceptor.ApiAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
*
* @author yizhong.liao
* @createTime 2021/9/2 15:10
*/
@Component
public class InterceptorConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private ApiAccessInterceptor apiAccessInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir = registry.addInterceptor(apiAccessInterceptor);
        ir.addPathPatterns("/api/**");
        ir.excludePathPatterns("/api/auth/login");
        ir.excludePathPatterns("/api/public/**");
        ir.excludePathPatterns("/errors/**");
    }
}
