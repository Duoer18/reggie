package com.duoer.reggie.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.dao.UserMapper;
import com.duoer.reggie.entity.User;
import com.duoer.reggie.service.UserService;
import com.duoer.reggie.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result login(Map<String, String> u) {
        String phoneNumber = u.get("phone");
        String codeFromUser = u.get("code");
        String code = redisTemplate.opsForValue().get("code_" + phoneNumber);

        if (StringUtils.isNotEmpty(phoneNumber)
                && StringUtils.isNotEmpty(codeFromUser)
                && codeFromUser.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phoneNumber);
            User user = getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phoneNumber);
                user.setStatus(1);
                save(user);
            }

            redisTemplate.delete("code_" + phoneNumber);

            String token = JwtUtils.createJWT(String.valueOf(user.getId()));
            redisTemplate.opsForValue().set("user_token_" + token, JSON.toJSONString(user),
                    60, TimeUnit.MINUTES);
            return Result.success(token);
        }

        return Result.failed("用户登录失败");
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("user_token_" + token);
        return Result.success("用户退出成功");
    }

    @Override
    public User checkToken(String token) {
        String idStr = JwtUtils.parseJWT(token);
        if (StringUtils.isEmpty(idStr)) {
            return null;
        }

        String userJSON = redisTemplate.opsForValue().get("user_token_" + token);
        if (StringUtils.isEmpty(userJSON)) {
            return null;
        }

        User user = JSON.parseObject(userJSON, User.class);
        if (user.getId() == null) {
            user.setId(Long.parseLong(idStr));
        }
        return user;
    }
}
