package com.duoer.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.dto.DishDto;
import com.duoer.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);
    DishDto getDishById(long id);
    boolean updateDish(DishDto dishDto);
    boolean deleteDishes(List<Long> ids);
    DishDto getDto(Dish dish, boolean setFlavors);
    List<DishDto> getDtoList(List<Dish> dishes ,boolean setFlavor);
    List<DishDto> listDishes(Dish dish);
}
