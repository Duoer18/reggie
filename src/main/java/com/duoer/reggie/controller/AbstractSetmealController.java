package com.duoer.reggie.controller;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.Setmeal;
import com.duoer.reggie.service.SetmealDishService;
import com.duoer.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AbstractSetmealController {
    @Autowired
    protected SetmealService setmealService;
    @Autowired
    protected SetmealDishService setmealDishService;

    public Result getSetmealList(Setmeal setmeal) {
        log.info("get setmeal categoryId={}", setmeal.getCategoryId());

        List<Setmeal> setmealList = setmealService.listSets(setmeal);
        return Result.success(setmealList);
    }
}
