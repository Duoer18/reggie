package com.duoer.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.Orders;
import com.duoer.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
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
        boolean isUpdated = ordersService.updateById(order);
        if (isUpdated) {
            return Result.success("订单修改成功");
        } else {
            return Result.failed("订单修改失败");
        }
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
