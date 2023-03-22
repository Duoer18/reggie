package com.duoer.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.takeout.common.ServiceException;
import com.duoer.takeout.dao.CategoryMapper;
import com.duoer.takeout.dao.DishMapper;
import com.duoer.takeout.dao.SetmealMapper;
import com.duoer.takeout.entity.Category;
import com.duoer.takeout.entity.Dish;
import com.duoer.takeout.entity.Setmeal;
import com.duoer.takeout.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public List<Category> listCategories(Category c) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(c.getType() != null, Category::getType, c.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        return list(queryWrapper);
    }

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

    @Override
    public Page<Category> listByPage(int page, int pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        Page<Category> categoryPage = new Page<>(page, pageSize);
        page(categoryPage, queryWrapper);
        return categoryPage;
    }
}
