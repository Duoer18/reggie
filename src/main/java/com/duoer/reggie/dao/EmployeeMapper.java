package com.duoer.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duoer.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
