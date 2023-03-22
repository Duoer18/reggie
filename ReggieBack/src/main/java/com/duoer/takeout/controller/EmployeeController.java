package com.duoer.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.entity.Employee;
import com.duoer.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody Employee e) {
        // 将密码加密
        e.setPassword(DigestUtils.md5DigestAsHex(e.getPassword().getBytes()));
        return employeeService.login(e);
    }

    /**
     * 员工退出接口
     */
    @PostMapping("/logout")
    public Result logout(@RequestHeader(name = "token", required = false, defaultValue = "") String token) {
        return employeeService.logout(token);
    }

    /**
     * 添加员工接口
     */
    @PostMapping
    public Result addEmployee(@RequestBody Employee e) {
        log.info("add {}", e);

        // 将员工信息添加到数据库
        boolean isSaved = employeeService.saveEmployee(e);

        // 返回添加状态
        if (isSaved) { // 员工添加成功
            log.info("添加员工: {}", e.getUsername());
            return Result.success("新增员工成功");
        } else { // 员工添加失败
            return Result.failed("新增员工失败");
        }
    }

    /**
     * 分页查询接口
     * @param page 页码
     * @param pageSize 页大小
     * @param name 员工名，可为空
     * @return 该页员工信息
     */
    @GetMapping("/page")
    public Result getEmployeesByPage(int page, int pageSize, @RequestParam(required = false) String name) {
        log.info("员工: 查询{}页，每页{}条", page, pageSize);

        Page<Employee> employeePage = employeeService.listByPage(page, pageSize, name);
        return Result.success(employeePage);
    }

    /**
     * 更改员工信息
     */
    @PutMapping
    public Result updateEmployee(@RequestBody Employee e) {
        log.info("update {}", e);

        if (e.getId() == 1) {
            e.setStatus(1);
        }

        boolean isUpdated = employeeService.updateById(e);
        if (isUpdated) {
            return Result.success("更新员工信息成功");
        } else {
            return Result.failed("更新员工状态失败");
        }
    }

    @GetMapping("/{id}")
    public Result getOneEmployee(@PathVariable long id) {
        log.info("获取id={}，待修改员工", id);

        Employee e = employeeService.getById(id);
        return Result.success(e);
    }
}