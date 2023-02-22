package com.duoer.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.dto.SetmealDishDto;
import com.duoer.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    List<SetmealDishDto> getDishes(long id);
    SetmealDishDto getDto(SetmealDish setmealDish);
}
