package com.duoer.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.dto.SetmealDishDto;
import com.duoer.takeout.dto.SetmealDto;
import com.duoer.takeout.service.SetmealDishService;
import com.duoer.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public Result getSetsByPage(int page, int pageSize, String name) {
        log.info("套餐: 查询{}页，每页{}条", page, pageSize);

        Page<SetmealDto> setmealDtoPage = setmealService.listByPage(page, pageSize, name);
        return Result.success(setmealDtoPage);
    }

    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDto setmealDto) {
        log.info("add {}", setmealDto);

        boolean isSaved = setmealService.addSetmeal(setmealDto);
        if (isSaved) {
            return Result.success("套餐添加成功");
        } else {
            return Result.failed("套餐添加失败");
        }
    }

    @GetMapping("/{id}")
    public Result getSetById(@PathVariable long id) {
        log.info("get set id={}", id);

        SetmealDto setmealDto = setmealService.getSetById(id);
        return Result.success(setmealDto);
    }

    @PutMapping
    public Result updateSet(@RequestBody SetmealDto setmealDto) {
        boolean isUpdated = setmealService.updateSetmeal(setmealDto);
        if (isUpdated) {
            return Result.success("套餐修改成功");
        } else {
            return Result.failed("套餐修改失败");
        }
    }

    @DeleteMapping
    public Result deleteSet(@RequestParam List<Long> ids) {
        log.info("delete set ids={}", ids);

        boolean isRemoved = setmealService.deleteSetmeal(ids);
        if (isRemoved) {
            return Result.success("套餐删除成功");
        } else {
            return Result.failed("套餐删除失败");
        }
    }

    @PostMapping("/status/{status}")
    public Result changeSetmealStatus(@PathVariable int status, @RequestParam List<Long> ids) {
        boolean isUpdated = setmealService.changeSetMealStatus(status, ids);
        if (isUpdated) {
            return Result.success("套餐状态修改成功");
        } else {
            return Result.failed("套餐状态修改失败");
        }
    }

    @GetMapping("/dish/{id}")
    public Result getSetmealDishes(@PathVariable long id) {
        log.info("get dish id={}", id);

        List<SetmealDishDto> setmealDishDtoList = setmealDishService.getDishes(id);
        return Result.success(setmealDishDtoList);
    }
}
