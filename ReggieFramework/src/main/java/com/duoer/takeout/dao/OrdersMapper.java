package com.duoer.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duoer.takeout.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
