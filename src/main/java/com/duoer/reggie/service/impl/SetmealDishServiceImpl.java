package com.duoer.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.dao.SetmealDishMapper;
import com.duoer.reggie.dto.DishDto;
import com.duoer.reggie.dto.SetmealDishDto;
import com.duoer.reggie.entity.SetmealDish;
import com.duoer.reggie.service.DishService;
import com.duoer.reggie.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
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
        SetmealDishDto setmealDishDto = new SetmealDishDto();
        BeanUtils.copyProperties(setmealDish, setmealDishDto);

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
