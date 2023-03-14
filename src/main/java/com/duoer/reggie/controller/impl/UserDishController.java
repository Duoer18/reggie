package com.duoer.reggie.controller.impl;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.controller.AbstractDishController;
import com.duoer.reggie.dto.DishDto;
import com.duoer.reggie.entity.Dish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/u-dish")
@Slf4j
public class UserDishController extends AbstractDishController {
    @GetMapping("/list")
    public Result getDishes(Dish dish) {
        log.info("get dishes in categoryId={} and status={}", dish.getCategoryId(), dish.getStatus());

        List<DishDto> dishDtoList = dishService.listDishesWithCache(dish);
        return Result.success(dishDtoList);
    }
}
