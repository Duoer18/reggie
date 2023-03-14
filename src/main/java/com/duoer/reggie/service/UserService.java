package com.duoer.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Result login(Map<String, String> u);

    Result logout(String token);

    User checkToken(String token);
}
