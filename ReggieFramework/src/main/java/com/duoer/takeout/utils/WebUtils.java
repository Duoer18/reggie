package com.duoer.takeout.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {
    public static void responseJson(HttpServletResponse response, Object result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
