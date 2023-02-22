package com.duoer.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.Category;
import com.duoer.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result addCategory(@RequestBody Category c) {
        log.info("add {}", c);

        boolean isSaved = categoryService.save(c);
        if (isSaved) {
            return Result.success("添加品类成功");
        } else {
            return Result.failed("添加品类失败");
        }
    }

    @GetMapping("/page")
    public Result getCategoriesByPage(int page, int pageSize) {
        log.info("品类: 查询{}页，每页{}条", page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        Page<Category> categoryPage = new Page<>(page, pageSize);
        categoryService.page(categoryPage, queryWrapper);
        return Result.success(categoryPage);
    }

    @GetMapping("/list")
    public Result getCategories(Category c) {
        log.info("品类");

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(c.getType() != null, Category::getType, c.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> categories = categoryService.list(queryWrapper);
        return Result.success(categories);
    }

    @DeleteMapping
    public Result deleteCategory(long id) {
        log.info("delete category id={}", id);

        boolean isRemoved = categoryService.removeById(id);
        if (isRemoved) {
            return Result.success("删除品类成功");
        } else {
            return Result.failed("删除品类失败");
        }
    }

    @PutMapping
    public Result updateCategory(@RequestBody Category c) {
        log.info("update {}", c);

        boolean isUpdated = categoryService.updateById(c);
        if (isUpdated) {
            return Result.success("品类修改成功");
        } else {
            return Result.failed("品类修改失败");
        }
    }

    @GetMapping("/{id}")
    public Result getOneCategory(@PathVariable long id) {
        log.info("获取id={}，待修改品类", id);

        Category c = categoryService.getById(id);
        return Result.success(c);
    }
}
