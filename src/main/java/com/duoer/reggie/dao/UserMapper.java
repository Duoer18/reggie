package com.duoer.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duoer.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
