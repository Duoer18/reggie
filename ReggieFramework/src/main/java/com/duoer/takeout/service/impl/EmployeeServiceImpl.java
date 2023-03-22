package com.duoer.takeout.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.takeout.common.BaseContext;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.dao.EmployeeMapper;
import com.duoer.takeout.dto.EmployeeDto;
import com.duoer.takeout.entity.Employee;
import com.duoer.takeout.service.EmployeeService;
import com.duoer.takeout.utils.BeanCopyUtils;
import com.duoer.takeout.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
        EmployeeDto employeeDto = BeanCopyUtils.convertBean(selectedEm, EmployeeDto.class);
        employeeDto.setToken(token);
        redisTemplate.opsForValue()
                .set("employee_id_" + selectedEm.getId(),
                        JSON.toJSONString(employeeDto),
                60,
                        TimeUnit.MINUTES);

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

        String employeeJSON = redisTemplate.opsForValue().get("employee_id_" + idStr);
        if (StringUtils.isEmpty(employeeJSON)) {
            return null;
        }

        EmployeeDto e = JSON.parseObject(employeeJSON, EmployeeDto.class);
        if (!token.equals(e.getToken())) {
            return null;
        }

        if (e.getId() == null) {
            e.setId(Long.parseLong(idStr));
        }
        return e;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("employee_id_" + BaseContext.getEId());
        return Result.success("退出成功");
    }

    @Override
    public boolean saveEmployee(Employee e) {
        // 插入前完整性保障
        // 设置id为null
        e.setId(null);
        // 设置初始密码
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        e.setPassword(password);

        // 将员工信息添加到数据库
        return save(e);
    }

    @Override
    public Page<Employee> listByPage(int page, int pageSize, String name) {
        // 设置查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 进行分页查询
        Page<Employee> employeePage = new Page<>(page, pageSize);
        page(employeePage, queryWrapper);
        return employeePage;
    }
}
