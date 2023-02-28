package com.duoer.reggie.interceptor;

import com.alibaba.fastjson.JSON;
import com.duoer.reggie.common.BaseContext;
import com.duoer.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        if (uid != null) { // 用户已登录
            log.info("Accessing {} granted", request.getRequestURI());
            BaseContext.setUid(uid);
            return true;
        }

        log.info("Accessing {} intercepted", request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
        return false;

    }
}
