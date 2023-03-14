package com.duoer.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.common.ServiceException;
import com.duoer.reggie.dao.SetmealMapper;
import com.duoer.reggie.dto.SetmealDto;
import com.duoer.reggie.entity.Category;
import com.duoer.reggie.entity.Setmeal;
import com.duoer.reggie.entity.SetmealDish;
import com.duoer.reggie.service.CategoryService;
import com.duoer.reggie.service.SetmealDishService;
import com.duoer.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Transactional
    @Override
    public boolean addSetmeal(SetmealDto setmealDto) {
        // 添加套餐到套餐表
        boolean isSaved = save(setmealDto);

        if (isSaved) {
            // 添加套餐菜品
            setmealDto.getSetmealDishes().forEach(dish -> dish.setSetmealId(setmealDto.getId()));
            setmealDishService.saveBatch(setmealDto.getSetmealDishes());

            // 清缓存
            redisTemplate.delete("setmeal_category_" + setmealDto.getCategoryId()
                    + "_status_" + setmealDto.getStatus());
        }

        return isSaved;
    }

    @Override
    public SetmealDto getDto(Setmeal setmeal, boolean setDishes) {
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        if (setDishes) {
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId())
                    .orderByAsc(SetmealDish::getSort)
                    .orderByDesc(SetmealDish::getUpdateTime);
            setmealDto.setSetmealDishes(setmealDishService.list(queryWrapper));
        }

        Category category = categoryService.getById(setmealDto.getCategoryId());
        if (category != null) {
            setmealDto.setCategoryName(category.getName());
        }

        return setmealDto;
    }

    @Override
    public List<SetmealDto> getDtoList(List<Setmeal> setmealList, boolean setDishes) {
        return setmealList
                .stream()
                .map(meal -> getDto(meal, setDishes))
                .collect(Collectors.toList());
    }

    @Override
    public SetmealDto getSetById(long id) {
        Setmeal setmeal = getById(id);
        return getDto(setmeal, true);
    }

    @Override
    public List<Setmeal> listSets(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId())
                .eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus())
                .orderByDesc(Setmeal::getUpdateTime);

        return list(queryWrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Setmeal> listSetMealsWithCache(Setmeal setmeal) {
        // 查缓存
        List<Setmeal> setmealList = (List<Setmeal>) redisTemplate.opsForValue()
                .get("setmeal_category_" + setmeal.getCategoryId() + "_status_" + setmeal.getStatus());
        if (setmealList != null && setmealList.size() > 0) {
            return setmealList;
        }

        // 未命中
        setmealList = listSets(setmeal);

        // 将菜品缓存
        redisTemplate.opsForValue()
                .set("setmeal_category_" + setmeal.getCategoryId() + "_status_" + setmeal.getStatus(),
                        setmealList);

        return setmealList;
    }

    @Transactional
    @Override
    public boolean updateSetmeal(SetmealDto setmealDto) {
        Long categoryId = setmealDto.getCategoryId();
        Integer status = setmealDto.getStatus();

        boolean isUpdated = updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        setmealDto.getSetmealDishes().forEach(dish -> dish.setSetmealId(setmealDto.getId()));
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());

        // 清缓存
        redisTemplate.delete("setmeal_category_" + categoryId + "_status_" + status);

        return isUpdated;
    }

    @Transactional
    @Override
    public boolean deleteSetmeal(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, 1);
        long validCounts = count(queryWrapper);
        if (validCounts > 0) {
            throw new ServiceException("删除失败，请先停售套餐");
        }

        boolean isRemoved = removeBatchByIds(ids);

        // 删除所有菜品
        LambdaQueryWrapper<SetmealDish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(dishQueryWrapper);
        return isRemoved;
    }
}
