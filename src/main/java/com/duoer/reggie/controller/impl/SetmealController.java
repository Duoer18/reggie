package com.duoer.reggie.controller.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.dto.SetmealDishDto;
import com.duoer.reggie.dto.SetmealDto;
import com.duoer.reggie.entity.Setmeal;
import com.duoer.reggie.service.SetmealDishService;
import com.duoer.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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

        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(StringUtils.isNotEmpty(name), Setmeal::getName, name)
                .orderByDesc(Setmeal::getUpdateTime);

        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        setmealService.page(setmealPage, setmealQueryWrapper);
        List<SetmealDto> setmealDtoList = setmealService.getDtoList(setmealPage.getRecords(), false);

        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        setmealDtoPage.setRecords(setmealDtoList);
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
