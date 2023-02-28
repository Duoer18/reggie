package com.duoer.reggie.controller;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.Orders;
import com.duoer.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@Slf4j
public class AbstractOrderController {
    @Autowired
    protected OrdersService ordersService;

    public Result updateOrder(@RequestBody Orders order) {
        boolean isUpdated = ordersService.updateById(order);
        if (isUpdated) {
            return Result.success("订单修改成功");
        } else {
            return Result.failed("订单修改失败");
        }
    }
}
