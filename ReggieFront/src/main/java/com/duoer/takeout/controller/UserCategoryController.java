package com.duoer.takeout.controller;

import com.duoer.takeout.common.Result;
import com.duoer.takeout.entity.Category;
import com.duoer.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class UserCategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result getCategories(Category c) {
        List<Category> categoryList = categoryService.listCategories(c);
        return Result.success(categoryList);
    }
}
