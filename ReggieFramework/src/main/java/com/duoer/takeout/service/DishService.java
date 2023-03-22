package com.duoer.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.takeout.dto.DishDto;
import com.duoer.takeout.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);
    DishDto getDishById(long id);

    List<DishDto> listDishesWithCache(Dish dish);

    boolean updateDish(DishDto dishDto);

    boolean changeDishStatus(int status, List<Long> ids);

    boolean deleteDishes(List<Long> ids);
    DishDto getDto(Dish dish, boolean setFlavors);
    List<DishDto> getDtoList(List<Dish> dishes ,boolean setFlavor);
    List<DishDto> listDishes(Dish dish);

    Page<DishDto> listByPage(int page, int pageSize, String name);
}
