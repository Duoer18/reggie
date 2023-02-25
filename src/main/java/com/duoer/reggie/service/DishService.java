package com.duoer.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.dto.DishDto;
import com.duoer.reggie.entity.Dish;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);
    DishDto getDishById(long id);
    boolean updateDish(DishDto dishDto);
    Map.Entry<Boolean, Set<Object>> deleteDishes(List<Long> ids);
    DishDto getDto(Dish dish, boolean setFlavors, boolean setCategory);
    List<DishDto> getDtoList(List<Dish> dishes ,boolean setFlavor, boolean setCategory);
    List<DishDto> listDishes(Dish dish);
    boolean deleteDishesNoCache(List<Long> ids);
}
