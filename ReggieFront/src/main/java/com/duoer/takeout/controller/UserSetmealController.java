package com.duoer.takeout.controller;

import com.duoer.takeout.common.Result;
import com.duoer.takeout.dto.SetmealDishDto;
import com.duoer.takeout.entity.Setmeal;
import com.duoer.takeout.service.SetmealDishService;
import com.duoer.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class UserSetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/list")
    public Result getSetmealList(Setmeal setmeal) {
        log.info("get setmeal categoryId={}", setmeal.getCategoryId());

        List<Setmeal> setmealList = setmealService.listSetMealsWithCache(setmeal);
        return Result.success(setmealList);
    }

    @GetMapping("/dish/{id}")
    public Result getSetmealDishes(@PathVariable long id) {
        log.info("get dish id={}", id);

        List<SetmealDishDto> setmealDishDtoList = setmealDishService.getDishes(id);
        return Result.success(setmealDishDtoList);
    }
}
