package com.duoer.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.entity.Orders;
import com.duoer.takeout.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/page")
    public Result getOrderByPage(int page, int pageSize, String number,
                                 String beginTime, String endTime) {
        log.info("get order page={}, size={}, number={}, begin time={}, end time={}",
                page, pageSize, number, beginTime, endTime);

        Page<? extends Orders> ordersPage = ordersService.getOrdersByPage(page, pageSize, number,
                beginTime, endTime, true);
        return Result.success(ordersPage);
    }

    @PutMapping
    public Result updateOrder(@RequestBody Orders order) {
        boolean isUpdated = ordersService.updateById(order);
        if (isUpdated) {
            return Result.success("订单修改成功");
        } else {
            return Result.failed("订单修改失败");
        }
    }
}
