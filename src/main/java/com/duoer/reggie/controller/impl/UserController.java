package com.duoer.reggie.controller.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.User;
import com.duoer.reggie.service.UserService;
import com.duoer.reggie.utils.JwtUtils;
import com.duoer.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public Result sendMsg(@RequestBody User user) throws Exception {
        String phoneNumber = user.getPhone();

        if (StringUtils.isNotEmpty(phoneNumber)) {
            // 发送验证码
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
//            MessageSender.send("阿里云短信测试", "SMS_154950909",
//                    phoneNumber, code);
            log.info("code={}", code);

            // 将验证码存到redis中
            redisTemplate.opsForValue().set("code_" + phoneNumber, code, 5, TimeUnit.MINUTES);

            return Result.success("验证码发送成功");
        }

        return Result.failed("验证码发送失败");
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String ,String> u, HttpSession session) {
        log.info("login user {}", u);

        String phoneNumber = u.get("phone");
        String codeFromUser = u.get("code");
        String code = redisTemplate.opsForValue().get("code_" + phoneNumber);

        if (StringUtils.isNotEmpty(phoneNumber)
                && StringUtils.isNotEmpty(codeFromUser)
                && codeFromUser.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phoneNumber);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phoneNumber);
                user.setStatus(1);
                userService.save(user);
            }

            redisTemplate.delete("code_" + phoneNumber);

            String token = JwtUtils.createJWT(String.valueOf(user.getId()));
            redisTemplate.opsForValue().set("user_token_" + token, JSON.toJSONString(user),
                    60, TimeUnit.MINUTES);
            return Result.success(token);
        }

        return Result.failed("用户登录失败");
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader(name = "token", required = false, defaultValue = "") String token) {
        redisTemplate.delete("user_token_" + token);
        return Result.success("用户退出成功");
    }
}
