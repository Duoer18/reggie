package com.duoer.reggie.config;

import com.duoer.reggie.interceptor.EmployeeLoginInterceptor;
import com.duoer.reggie.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private EmployeeLoginInterceptor employeeLoginInterceptor;
    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(employeeLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/employee/login", "/backend/**", "/front/**", "/common/**",
                        "/user/login", "/user/sendMsg");
//        registry.addInterceptor(employeeLoginInterceptor)
//                .addPathPatterns("/employee/**", "/category/**", "/dish/**", "/setmeal/**")
//                .excludePathPatterns("/employee/login");
    }
}
