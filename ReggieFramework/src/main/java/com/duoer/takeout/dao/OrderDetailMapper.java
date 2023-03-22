package com.duoer.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duoer.takeout.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
