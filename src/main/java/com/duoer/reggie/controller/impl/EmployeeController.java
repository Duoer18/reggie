package com.duoer.reggie.controller.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.reggie.entity.Employee;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
    public Result login(@RequestBody Employee e, HttpSession session) {
        // 将密码加密
        String password = e.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 验证用户名密码是否正确
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, e.getUsername())
                .eq(Employee::getPassword, password);
        Employee selectedEm = employeeService.getOne(queryWrapper);

        if (selectedEm == null) { // 用户名或密码错误
            return Result.failed("登录失败");
        }

        if (selectedEm.getStatus() == 0) { // 账号状态异常
            return Result.failed("账号已禁用");
        }

        // 用户登录成功
        log.info("员工 {} 登录", selectedEm.getUsername());
        session.setAttribute("employee", selectedEm.getId());
        return Result.success(selectedEm);
    }

    /**
     * 员工退出接口
     */
    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        session.removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 添加员工接口
     */
    @PostMapping
    public Result addEmployee(@RequestBody Employee e) {
        log.info("add {}", e);

        // 插入前完整性保障
        // 设置id为null
        e.setId(null);
        // 设置初始密码
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        e.setPassword(password);
//        // 设置添加时间
//        LocalDateTime addTimeStamp = LocalDateTime.now();
//        e.setCreateTime(addTimeStamp);
//        e.setUpdateTime(addTimeStamp);
//        // 设置执行添加的用户
//        Long id = (Long) session.getAttribute("employee");
//        e.setCreateUser(id);
//        e.setUpdateUser(id);

        // 将员工信息添加到数据库
        boolean isSaved = employeeService.save(e);

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

        // 设置查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 进行分页查询
        Page<Employee> employeePage = new Page<>(page, pageSize);
        employeeService.page(employeePage, queryWrapper);
        return Result.success(employeePage);
    }

    /**
     * 更改员工信息
     */
    @PutMapping
    public Result updateEmployee(@RequestBody Employee e, HttpSession session) {
        log.info("update {}", e);

        long operatorId = (long) session.getAttribute("employee");
        if (operatorId == 1 || operatorId == (e.getId())) {
            // 理论上需要查找要修改的用户的角色
            if (e.getId() == 1) {
                e.setStatus(1);
            }

//            e.setUpdateUser(operatorId);
//            e.setUpdateTime(LocalDateTime.now());

            boolean isUpdated = employeeService.updateById(e);
            if (isUpdated) {
                return Result.success("更新员工信息成功");
            } else {
                return Result.failed("更新员工状态失败");
            }
        } else {
            return Result.failed("你不可修改该员工信息");
        }
    }

    @GetMapping("/{id}")
    public Result getOneEmployee(@PathVariable long id, HttpSession session) {
        log.info("获取id={}，待修改员工", id);

        long operatorId = (long) session.getAttribute("employee");
        if (operatorId == 1 || operatorId == id) {
            Employee e = employeeService.getById(id);
            return Result.success(e);
        } else {
            return Result.failed("你不可修改该员工信息");
        }
    }
}