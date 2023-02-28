package com.duoer.reggie.controller;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.dto.DishDto;
import com.duoer.reggie.entity.Dish;
import com.duoer.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public abstract class AbstractDishController {
    @Autowired
    protected DishService dishService;

    public Result getDishes(Dish dish) {
        log.info("get dishes in categoryId={} and status={}", dish.getCategoryId(), dish.getStatus());

        List<DishDto> dishDtoList = dishService.listDishes(dish);
        return Result.success(dishDtoList);
    }
}
