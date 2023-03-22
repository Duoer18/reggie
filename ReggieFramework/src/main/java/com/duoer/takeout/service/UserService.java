package com.duoer.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Result login(Map<String, String> u);

    Result logout(String token);

    User checkToken(String token);
}
