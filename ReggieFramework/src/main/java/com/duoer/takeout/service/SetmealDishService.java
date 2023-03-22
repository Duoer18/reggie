package com.duoer.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.takeout.dto.SetmealDishDto;
import com.duoer.takeout.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    List<SetmealDishDto> getDishes(long id);
    SetmealDishDto getDto(SetmealDish setmealDish);
}
