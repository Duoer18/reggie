package com.duoer.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.takeout.common.Result;
import com.duoer.takeout.dto.DishDto;
import com.duoer.takeout.entity.Dish;
import com.duoer.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 菜品添加接口
     */
    @PostMapping
    public Result addDish(@RequestBody DishDto dishDto) {
        log.info("add dish {}", dishDto);

        dishDto.setStatus(1);
        boolean isSaved = dishService.saveWithFlavor(dishDto);
        if (isSaved) {
            return Result.success("菜品添加成功");
        } else {
            return Result.failed("菜品添加失败");
        }
    }

    /**
     * 分页查询菜品接口
     */
    @GetMapping("/page")
    public Result getDishesByPage(int page, int pageSize, String name) {
        log.info("菜品: 查询{}页，每页{}条", page, pageSize);

        Page<DishDto> dishDtoPage = dishService.listByPage(page, pageSize, name);
        return Result.success(dishDtoPage);
    }

    @PutMapping
    public Result updateDish(@RequestBody DishDto dishDto) {
        log.info("update {}", dishDto);

        boolean isUpdated = dishService.updateDish(dishDto);
        if (isUpdated) {
            return Result.success("菜品修改成功");
        } else {
            return Result.failed("菜品修改失败");
        }
    }

    @GetMapping("/{id}")
    public Result getOneDish(@PathVariable long id) {
        log.info("get dish id={}", id);

        DishDto dishDto = dishService.getDishById(id);
        return Result.success(dishDto);
    }

    @PostMapping("/status/{status}")
    public Result changeDishStatus(@PathVariable int status, @RequestParam List<Long> ids) {
        boolean isUpdated = dishService.changeDishStatus(status, ids);
        if (isUpdated) {
            return Result.success("菜品状态修改成功");
        } else {
            return Result.failed("菜品状态修改失败");
        }
    }

    @DeleteMapping
    public Result deleteDishes(@RequestParam List<Long> ids) {
        log.info("delete dishes ids={}", ids);

        boolean isRemoved = dishService.deleteDishes(ids);
        if (isRemoved) {
            return Result.success("删除菜品成功");
        } else {
            return Result.failed("删除菜品失败");
        }
    }

    @GetMapping("/list")
    public Result getDishes(Dish dish) {
        log.info("get dishes in categoryId={} and status={}", dish.getCategoryId(), dish.getStatus());

        List<DishDto> dishDtoList = dishService.listDishes(dish);
        return Result.success(dishDtoList);
    }
}
