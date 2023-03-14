package com.duoer.reggie.controller.impl;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.User;
import com.duoer.reggie.service.UserService;
import com.duoer.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

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
    public Result login(@RequestBody Map<String ,String> u) {
        log.info("login user {}", u);
        return userService.login(u);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader(name = "token", required = false, defaultValue = "") String token) {
        return userService.logout(token);
    }
}
