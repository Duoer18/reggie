package com.duoer.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.common.ServiceException;
import com.duoer.reggie.dao.CategoryMapper;
import com.duoer.reggie.dao.DishMapper;
import com.duoer.reggie.dao.SetmealMapper;
import com.duoer.reggie.entity.Category;
import com.duoer.reggie.entity.Dish;
import com.duoer.reggie.entity.Setmeal;
import com.duoer.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public boolean removeById(long id) {
        // 检查当前品类下是否仍存有菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        if (dishMapper.selectCount(dishLambdaQueryWrapper) > 0) {
            throw new ServiceException("删除品类失败，当前品类下仍存有菜品");
        }

        // 检查当前品类下是否仍存有套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealMapper.selectCount(setmealLambdaQueryWrapper) > 0) {
            throw new ServiceException("删除品类失败，当前品类下仍存有套餐");
        }

        return super.removeById(id);
    }
}
