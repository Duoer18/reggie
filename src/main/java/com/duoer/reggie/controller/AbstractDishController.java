package com.duoer.reggie.controller;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.Dish;
import com.duoer.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class AbstractDishController {
    @Autowired
    protected DishService dishService;

    public abstract Result getDishes(Dish dish);
}
