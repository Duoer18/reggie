package com.duoer.reggie.controller.impl;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.controller.AbstractCategoryController;
import com.duoer.reggie.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/u-category")
@Slf4j
public class UserCategoryController extends AbstractCategoryController {
    @GetMapping("/list")
    public Result getCategories(Category c) {
        return super.getCategories(c);
    }
}
