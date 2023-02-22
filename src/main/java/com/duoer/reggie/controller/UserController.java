package com.duoer.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.User;
import com.duoer.reggie.service.UserService;
import com.duoer.reggie.utils.MessageSender;
import com.duoer.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result sendMsg(@RequestBody User user, HttpSession session) throws Exception {
        String phoneNumber = user.getPhone();

        if (StringUtils.isNotEmpty(phoneNumber)) {
            // 发送验证码
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
//            MessageSender.send("阿里云短信测试", "SMS_154950909",
//                    phoneNumber, code);
            log.info("code={}", code);
            session.setAttribute("code", code);
            return Result.success("验证码发送成功");
        }

        return Result.failed("验证码发送失败");
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String ,String> u, HttpSession session) {
        log.info("login user {}", u);

        String code = (String) session.getAttribute("code");
        String phoneNumber = u.get("phone");
        String codeFromUser = u.get("code");

        if (StringUtils.isNotEmpty(phoneNumber) && codeFromUser != null && codeFromUser.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phoneNumber);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phoneNumber);
                user.setStatus(1);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());
            session.removeAttribute("code");
            return Result.success(user);
        }

        return Result.failed("用户登录失败");
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        session.removeAttribute("user");
        return Result.success("用户退出成功");
    }
}
