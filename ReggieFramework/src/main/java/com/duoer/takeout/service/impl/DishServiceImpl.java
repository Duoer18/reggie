package com.duoer.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.takeout.common.ServiceException;
import com.duoer.takeout.dao.DishMapper;
import com.duoer.takeout.dto.DishDto;
import com.duoer.takeout.entity.Category;
import com.duoer.takeout.entity.Dish;
import com.duoer.takeout.entity.DishFlavor;
import com.duoer.takeout.service.CategoryService;
import com.duoer.takeout.service.DishFlavorService;
import com.duoer.takeout.service.DishService;
import com.duoer.takeout.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Transactional
    @Override
    public boolean saveWithFlavor(DishDto dishDto) {
        boolean isSaved = save(dishDto);

        if (isSaved) {
            // 添加所有口味
            dishDto.getFlavors().forEach((dishFlavor -> dishFlavor.setDishId(dishDto.getId())));
            dishFlavorService.saveBatch(dishDto.getFlavors());

            // 清除菜品缓存
            redisTemplate.delete("dish_category_" + dishDto.getCategoryId()
                    + "_status_" + dishDto.getStatus());
        }

        return isSaved;
    }

    @Override
    public DishDto getDto(Dish dish, boolean setFlavors) {
        DishDto dishDto = BeanCopyUtils.convertBean(dish, DishDto.class);

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

    @Override
    public Page<DishDto> listByPage(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);

        page(dishPage, queryWrapper);
        List<DishDto> dishDtoList = getDtoList(dishPage.getRecords(), false);

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        dishDtoPage.setRecords(dishDtoList);
        return dishDtoPage;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DishDto> listDishesWithCache(Dish dish) {
        // 查缓存
        List<DishDto> dishDtoList = (List<DishDto>) redisTemplate.opsForValue()
                .get("dish_category_" + dish.getCategoryId() + "_status_" + dish.getStatus());
        if (!CollectionUtils.isEmpty(dishDtoList)) {
            return dishDtoList;
        }

        // 未命中
        dishDtoList = listDishes(dish);

        // 将菜品缓存
        redisTemplate.opsForValue()
                .set("dish_category_" + dish.getCategoryId() + "_status_" + dish.getStatus(),
                        dishDtoList,
                        60,
                        TimeUnit.MINUTES);
        return dishDtoList;
    }

    @Transactional
    @Override
    public boolean updateDish(DishDto dishDto) {
        Long categoryId = dishDto.getCategoryId();
        Integer status = dishDto.getStatus();

        boolean isUpdated = updateById(dishDto);

        // 删除原口味
        LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorQueryWrapper);

        // 添加新口味
        dishDto.getFlavors().forEach((dishFlavor -> dishFlavor.setDishId(dishDto.getId())));
        dishFlavorService.saveBatch(dishDto.getFlavors());

        // 清除菜品缓存
        redisTemplate.delete("dish_category_" + categoryId + "_status_" + status);

        return isUpdated;
    }

    @Override
    public boolean changeDishStatus(int status, List<Long> ids) {
        List<Dish> dishes = ids.stream()
                .map(id -> {
                    Dish dish = new Dish();
                    dish.setId(id);
                    dish.setStatus(status);
                    return dish;
                })
                .collect(Collectors.toList());
        boolean isUpdated = updateBatchById(dishes);

        if (isUpdated) {
            // 清缓存
            Set<Long> categories = listByIds(ids).stream()
                    .map(Dish::getCategoryId)
                    .collect(Collectors.toSet());
            for (Long categoryId : categories) {
                redisTemplate.delete("dish_category_" + categoryId + "_status_1");
            }
        }

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
