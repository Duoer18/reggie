package com.duoer.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    boolean removeById(long id);
}
