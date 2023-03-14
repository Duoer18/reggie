package com.duoer.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Result login(Employee e);

    Employee checkToken(String token);

    Result logout(String token);
}
