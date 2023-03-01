package com.duoer.reggie.interceptor;

import com.alibaba.fastjson.JSON;
import com.duoer.reggie.common.BaseContext;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class EmployeeLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            log.info("Accessing {} intercepted", request.getRequestURI());
            response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
            return false;
        }

        String idStr = JwtUtils.parseJWT(token);
        if (StringUtils.isEmpty(idStr)) {
            log.info("Accessing {} intercepted", request.getRequestURI());
            response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
            return false;
        }

        String userJSON = redisTemplate.opsForValue().get("employee_token_" + token);
        if (StringUtils.isEmpty(userJSON)) {
            log.info("Accessing {} intercepted", request.getRequestURI());
            response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
            return false;
        }

        BaseContext.setEid(Long.parseLong(idStr));
        log.info("Accessing {} granted", request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeEid();
    }
}
