package com.duoer.reggie.controller.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.controller.AbstractOrderController;
import com.duoer.reggie.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/u-order")
@Slf4j
public class UserOrderController extends AbstractOrderController {
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
