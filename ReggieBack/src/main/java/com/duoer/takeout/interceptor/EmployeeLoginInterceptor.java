package com.duoer.takeout.interceptor;

import com.alibaba.fastjson.JSON;
import com.duoer.takeout.common.BaseContext;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.entity.Employee;
import com.duoer.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class EmployeeLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private EmployeeService employeeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        Employee e = employeeService.checkToken(token);
        if (e == null) {
            log.info("Accessing {} intercepted", request.getRequestURI());
            response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
            return false;
        }

        BaseContext.setEid(e.getId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeEid();
    }
}
