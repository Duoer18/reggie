package com.duoer.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duoer.takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
