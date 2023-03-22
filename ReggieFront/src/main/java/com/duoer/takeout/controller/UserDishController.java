package com.duoer.takeout.controller;

import com.duoer.takeout.common.Result;
import com.duoer.takeout.dto.DishDto;
import com.duoer.takeout.entity.Dish;
import com.duoer.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class UserDishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result getDishes(Dish dish) {
        log.info("get dishes in categoryId={} and status={}", dish.getCategoryId(), dish.getStatus());

        List<DishDto> dishDtoList = dishService.listDishesWithCache(dish);
        return Result.success(dishDtoList);
    }
}
