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
public class UserOrderController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public Result submitOrder(@RequestBody Orders order) {
        log.info("add {}", order);

        boolean isSaved = ordersService.addOrder(order);
        if (isSaved) {
            return Result.success(order);
        } else {
            return Result.failed("订单添加失败");
        }
    }

    @GetMapping("/userPage")
    public Result getUserOrderByPage(int page, int pageSize) {
        log.info("get order page={}, size={}", page, pageSize);

        Page<? extends Orders> ordersPage = ordersService.getUserOrdersByPage(page, pageSize, true);
        return Result.success(ordersPage);
    }

    @PostMapping("/again")
    public Result orderAgain(@RequestBody Orders order) {
        log.info("again {}", order);

        boolean isSaved = ordersService.orderAgain(order);
        if (isSaved) {
            return Result.success(order);
        } else {
            return Result.failed("订单添加失败");
        }
    }
}
