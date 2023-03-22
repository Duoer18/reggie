package com.duoer.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.takeout.dao.SetmealDishMapper;
import com.duoer.takeout.dto.DishDto;
import com.duoer.takeout.dto.SetmealDishDto;
import com.duoer.takeout.entity.SetmealDish;
import com.duoer.takeout.service.DishService;
import com.duoer.takeout.service.SetmealDishService;
import com.duoer.takeout.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
        implements SetmealDishService {
    @Autowired
    private DishService dishService;

    @Override
    public SetmealDishDto getDto(SetmealDish setmealDish) {
        SetmealDishDto setmealDishDto = BeanCopyUtils.convertBean(setmealDish, SetmealDishDto.class);

        DishDto dish = dishService.getDishById(setmealDishDto.getDishId());
        setmealDishDto.setImage(dish.getImage());
        return setmealDishDto;
    }

    @Override
    public List<SetmealDishDto> getDishes(long id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id)
                .orderByAsc(SetmealDish::getSort)
                .orderByDesc(SetmealDish::getUpdateTime);

        return list(queryWrapper)
                .stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }
}
