package com.duoer.reggie.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.dao.EmployeeMapper;
import com.duoer.reggie.entity.Employee;
import com.duoer.reggie.service.EmployeeService;
import com.duoer.reggie.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result login(Employee e) {
        // 验证用户名密码是否正确
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, e.getUsername())
                .eq(Employee::getPassword, e.getPassword());
        Employee selectedEm = getOne(queryWrapper);

        if (selectedEm == null) { // 用户名或密码错误
            return Result.failed("登录失败");
        }

        if (selectedEm.getStatus() == 0) { // 账号状态异常
            return Result.failed("账号已禁用");
        }

        // 用户登录成功
        log.info("员工 {} 登录", selectedEm.getUsername());
        String token = JwtUtils.createJWT(String.valueOf(selectedEm.getId()));
        redisTemplate.opsForValue().set("employee_token_" + token, com.alibaba.fastjson.JSON.toJSONString(selectedEm),
                60, TimeUnit.MINUTES);

        Result success = Result.success(selectedEm);
        success.setMsg(token);
        return success;
    }

    @Override
    public Employee checkToken(String token) {
        String idStr = JwtUtils.parseJWT(token);
        if (StringUtils.isEmpty(idStr)) {
            return null;
        }

        String employeeJSON = redisTemplate.opsForValue().get("employee_token_" + token);
        if (StringUtils.isEmpty(employeeJSON)) {
            return null;
        }

        Employee e = JSON.parseObject(employeeJSON, Employee.class);
        if (e.getId() == null) {
            e.setId(Long.parseLong(idStr));
        }
        return e;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("employee_token_" + token);
        return Result.success("退出成功");
    }
}
