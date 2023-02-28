package com.duoer.reggie.controller.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.controller.AbstractOrderController;
import com.duoer.reggie.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController extends AbstractOrderController {
    @GetMapping("/page")
    public Result getOrderByPage(int page, int pageSize, String number,
                                 String beginTime, String endTime) {
        log.info("get order page={}, size={}, number={}, begin time={}, end time={}",
                page, pageSize, number, beginTime, endTime);

        Page<? extends Orders> ordersPage = ordersService.getAllOrdersByPage(page, pageSize, number,
                beginTime, endTime, true);
        return Result.success(ordersPage);
    }

    @PutMapping
    public Result updateOrder(@RequestBody Orders order) {
        return super.updateOrder(order);
    }
}
