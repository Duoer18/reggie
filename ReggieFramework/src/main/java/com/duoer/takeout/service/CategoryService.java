package com.duoer.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.takeout.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> listCategories(Category c);

    boolean removeById(long id);

    Page<Category> listByPage(int page, int pageSize);
}
