package com.duoer.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.common.ServiceException;
import com.duoer.reggie.dao.DishMapper;
import com.duoer.reggie.dto.DishDto;
import com.duoer.reggie.entity.Category;
import com.duoer.reggie.entity.Dish;
import com.duoer.reggie.entity.DishFlavor;
import com.duoer.reggie.service.CategoryService;
import com.duoer.reggie.service.DishFlavorService;
import com.duoer.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @Transactional
    @Override
    public boolean saveWithFlavor(DishDto dishDto) {
        boolean isSaved = save(dishDto);

        // 添加所有口味
        dishDto.getFlavors().forEach((dishFlavor -> dishFlavor.setDishId(dishDto.getId())));
        dishFlavorService.saveBatch(dishDto.getFlavors());

        return isSaved;
    }

    @Override
    public DishDto getDto(Dish dish, boolean setFlavors) {
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        if (setFlavors) {
            LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
            flavorQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
            dishDto.setFlavors(dishFlavorService.list(flavorQueryWrapper));
        }

        Category category = categoryService.getById(dishDto.getCategoryId());
        if (category != null) {
            dishDto.setCategoryName(category.getName());
        }

        return dishDto;
    }

    @Override
    public List<DishDto> getDtoList(List<Dish> dishes ,boolean setFlavor) {
        return dishes
                .stream()
                .map(dish -> getDto(dish, setFlavor))
                .collect(Collectors.toList());
    }

    @Override
    public DishDto getDishById(long id) {
        Dish dish = getById(id);
        if (dish != null) {
            return getDto(dish, true);
        } else {
            throw new ServiceException("菜品不存在");
        }
    }

    @Override
    public List<DishDto> listDishes(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .eq(dish.getStatus() != null, Dish::getStatus, dish.getStatus())
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = list(queryWrapper);
        return getDtoList(dishes, true);
    }

    @Transactional
    @Override
    public boolean updateDish(DishDto dishDto) {
        boolean isUpdated = updateById(dishDto);

        // 删除原口味
        LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorQueryWrapper);

        // 添加新口味
        dishDto.getFlavors().forEach((dishFlavor -> dishFlavor.setDishId(dishDto.getId())));
        dishFlavorService.saveBatch(dishDto.getFlavors());
        return isUpdated;
    }

    @Transactional
    @Override
    public boolean deleteDishes(List<Long> ids) {
        // 查询ids中所有在售菜品
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids)
                .eq(Dish::getStatus, 1)
                .select(Dish::getId);
        long validCounts = count(queryWrapper);
        if (validCounts > 0) { // 有在售菜品
            throw new ServiceException("删除失败，请先停售菜品");
        }

        // 删除所有菜品
        boolean isRemoved = removeBatchByIds(ids);

        // 删除所有口味
        LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
        flavorQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(flavorQueryWrapper);

        return isRemoved;
    }

}
