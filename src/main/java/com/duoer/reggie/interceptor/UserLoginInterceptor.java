package com.duoer.reggie.interceptor;

import com.alibaba.fastjson.JSON;
import com.duoer.reggie.common.BaseContext;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.User;
import com.duoer.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        User user = userService.checkToken(token);
        if (user == null) {
            log.info("Accessing {} intercepted", request.getRequestURI());
            response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
            return false;
        }

        BaseContext.setUid(user.getId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeUid();
    }
}
