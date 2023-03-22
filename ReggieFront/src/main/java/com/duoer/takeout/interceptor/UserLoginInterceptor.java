package com.duoer.takeout.interceptor;

import com.duoer.takeout.common.BaseContext;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.entity.User;
import com.duoer.takeout.service.UserService;
import com.duoer.takeout.utils.WebUtils;
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
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String token = request.getHeader("token");

        User user = userService.checkToken(token);
        if (user == null) {
            log.info("Accessing {} intercepted", request.getRequestURI());
            WebUtils.responseJson(response, Result.failed("NOTLOGIN"));
            return false;
        }

        BaseContext.setUid(user.getId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        BaseContext.removeUid();
    }
}
