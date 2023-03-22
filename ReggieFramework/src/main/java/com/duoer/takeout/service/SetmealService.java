package com.duoer.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.takeout.dto.SetmealDto;
import com.duoer.takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    boolean addSetmeal(SetmealDto setmealDto);
    SetmealDto getSetById(long id);

    List<Setmeal> listSetMealsWithCache(Setmeal setmeal);

    boolean updateSetmeal(SetmealDto setmealDto);

    boolean changeSetMealStatus(int status, List<Long> ids);

    boolean deleteSetmeal(List<Long> ids);
    SetmealDto getDto(Setmeal setmeal, boolean setDishes);
    List<SetmealDto> getDtoList(List<Setmeal> setmealList, boolean setDishes);
    List<Setmeal> listSets(Setmeal setmeal);

    Page<SetmealDto> listByPage(int page, int pageSize, String name);
}
