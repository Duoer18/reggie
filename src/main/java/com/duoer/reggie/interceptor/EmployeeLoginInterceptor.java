package com.duoer.reggie.interceptor;

import com.alibaba.fastjson.JSON;
import com.duoer.reggie.common.BaseContext;
import com.duoer.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class EmployeeLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long eid = (Long) session.getAttribute("employee");
        if (eid != null) { // 员工已登录
            log.info("Accessing {} granted", request.getRequestURI());
            BaseContext.setEid(eid);
            return true;
        }
        Long uid = (Long) session.getAttribute("user");
        if (uid != null) { // 用户已登录
            log.info("Accessing {} granted", request.getRequestURI());
            BaseContext.setEid(uid);
            return true;
        }
        log.info("Accessing {} intercepted", request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
        return false;
//        // 获取员工和用户id
//        HttpSession session = request.getSession();
//        Long eid = (Long) session.getAttribute("employee");
//        Long uid = (Long) session.getAttribute("user");
//
//        // 获取请求方式
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        if (PatternMatchUtils.simpleMatch("/employee/**", uri) // 员工资源只允许员工访问
//                || !method.equalsIgnoreCase("GET")) { // POST、PUT、DELETE方式只允许员工访问
//            if (eid != null) { // 员工已登录
//                log.info("Accessing {} granted", request.getRequestURI());
//                BaseContext.setEid(eid);
//                return true;
//            } else { // 员工未登录
//                log.info("Accessing {} intercepted", request.getRequestURI());
//                response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
//                return false;
//            }
//        } else { // GET方式允许任何登录后用户访问
//            if (eid != null || uid != null) { // 用户已登录
//                log.info("Accessing {} granted", request.getRequestURI());
//                if (eid != null) {
//                    BaseContext.setEid(eid);
//                }
//                if (uid != null) {
//                    BaseContext.setUid(uid);
//                }
//                return true;
//            } else { // 用户未登录
//                log.info("Accessing {} intercepted", request.getRequestURI());
//                response.getWriter().write(JSON.toJSONString(Result.failed("NOTLOGIN")));
//                return false;
//            }
//        }
    }
}
