package com.duoer.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Result login(Employee e);

    Employee checkToken(String token);

    Result logout(String token);

    boolean saveEmployee(Employee e);

    Page<Employee> listByPage(int page, int pageSize, String name);
}
