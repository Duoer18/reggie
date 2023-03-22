package com.duoer.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duoer.takeout.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
