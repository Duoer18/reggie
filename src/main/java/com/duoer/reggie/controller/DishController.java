package com.duoer.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.dto.DishDto;
import com.duoer.reggie.entity.Dish;
import com.duoer.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 菜品添加接口
     */
    @PostMapping
    public Result addDish(@RequestBody DishDto dishDto) {
        log.info("add dish {}", dishDto);

        dishDto.setStatus(1);
        boolean isSaved = dishService.saveWithFlavor(dishDto);
        if (isSaved) {
            // 清除redis中缓存
            String key = "dishes:cid=" + dishDto.getCategoryId() + ";status=1";
            log.info("delete redis key={}", key);
            redisTemplate.delete(key);

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

        Page<Dish> dishPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);

        dishService.page(dishPage, queryWrapper);
        List<DishDto> dishDtoList = dishService.getDtoList(dishPage.getRecords(),
                false, true);

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        dishDtoPage.setRecords(dishDtoList);
        return Result.success(dishDtoPage);
    }

    @PutMapping
    public Result updateDish(@RequestBody DishDto dishDto) {
        log.info("update {}", dishDto);

        boolean isUpdated = dishService.updateDish(dishDto);
        if (isUpdated) {
            // 清除redis中缓存
            String key = "dishes:cid=" + dishDto.getCategoryId() + ";status=1";
            log.info("delete redis key={}", key);
            redisTemplate.delete(key);

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
    public Result changeDishStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("change status={} on ids={}", status, ids);

        if (status == null || ids == null || ids.size() == 0) {
            return Result.failed("invalid request");
        }

        // 将ids集合转化为菜品集合
        List<Dish> dishes = ids.stream()
                .map(id -> {
                    Dish dish = new Dish();
                    dish.setId(id);
                    dish.setStatus(status);
                    return dish;
                })
                .collect(Collectors.toList());

        // 更新菜品状态
        boolean isUpdated = dishService.updateBatchById(dishes);
        if (isUpdated) {
            // 清除redis中缓存
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Dish::getId, ids);
            List<Object> keys = dishService.list(queryWrapper)
                    .stream()
                    .map(dish -> (Object) ("dishes:cid=" + dish.getCategoryId() + ";status=1"))
                    .collect(Collectors.toList());
            redisTemplate.delete(keys);

            return Result.success("菜品状态修改成功");
        } else {
            return Result.failed("菜品状态修改失败");
        }
    }

    @DeleteMapping
    public Result deleteDishes(@RequestParam List<Long> ids) {
        log.info("delete dishes ids={}", ids);

        // 删除菜品并获取菜品在redis中的key
        boolean isRemoved = dishService.deleteDishesNoCache(ids);
        if (isRemoved) {
            return Result.success("删除菜品成功");
        } else {
            return Result.failed("删除菜品失败");
        }
//        Map.Entry<Boolean, List<Object>> isRemovedFlagAndRedisKeys = dishService.deleteDishes(ids);
//        if (isRemovedFlagAndRedisKeys.getKey()) {
//            redisTemplate.delete(isRemovedFlagAndRedisKeys.getValue());
//            return Result.success("删除菜品成功");
//        } else {
//            return Result.failed("删除菜品失败");
//        }
    }

    @GetMapping("/list")
    @SuppressWarnings("unchecked")
    public Result getDishes(Dish dish) {
        log.info("get dishes in categoryId={} and status={}", dish.getCategoryId(), dish.getStatus());

        if (dish.getCategoryId() == null || dish.getStatus() == null) {
            return Result.failed("invalid request");
        }

        // 从redis中获取菜品
        String key = "dishes:cid=" + dish.getCategoryId() + ";status=" + dish.getStatus();
        List<DishDto> dishDtoList;
        if ((dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key)) == null) {
            dishDtoList = dishService.listDishes(dish);
            redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        }

        return Result.success(dishDtoList);
    }
}
