package com.duoer.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.Category;
import com.duoer.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public abstract class AbstractCategoryController {
    @Autowired
    protected CategoryService categoryService;

    public Result getCategories(Category c) {
        log.info("品类");

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(c.getType() != null, Category::getType, c.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> categories = categoryService.list(queryWrapper);
        return Result.success(categories);
    }
}
