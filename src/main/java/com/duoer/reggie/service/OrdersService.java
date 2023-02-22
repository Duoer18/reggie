package com.duoer.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

public interface OrdersService extends IService<Orders> {
    boolean addOrder(Orders order);
    Page<? extends Orders> getUserOrdersByPage(int page, int pageSize, boolean withDetails);
    Page<? extends Orders> getAllOrdersByPage(int page, int pageSize, String number,
                                              String beginTime, String endTime,
                                              boolean withDetails);
    Page<? extends Orders> getOrdersByPage(int page, int pageSize, boolean withDetails, LambdaQueryWrapper<Orders> queryWrapper);

    @Transactional
    boolean orderAgain(Orders order);
}
