package com.duoer.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duoer.reggie.common.ServiceException;
import com.duoer.reggie.dao.ShoppingCartMapper;
import com.duoer.reggie.entity.ShoppingCart;
import com.duoer.reggie.service.ShoppingCartService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public List<ShoppingCart> listCarts(long uid) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, uid)
                .gt(ShoppingCart::getNumber, 0);
        return list(queryWrapper);
    }

    private ShoppingCart getExist(@NotNull ShoppingCart cart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, cart.getUserId());
        if (cart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, cart.getDishId());
//            if (cart.getDishFlavor() != null) {
//                queryWrapper.eq(ShoppingCart::getDishFlavor, cart.getDishFlavor());
//            } else {
//                queryWrapper.isNull(ShoppingCart::getDishFlavor);
//            }
        } else if (cart.getSetmealId() != null){
            queryWrapper.eq(ShoppingCart::getSetmealId, cart.getSetmealId());
        } else {
            throw new ServiceException("未选择菜品或套餐");
        }

        return getOne(queryWrapper);
    }

    @Transactional
    @Override
    public boolean addItem(ShoppingCart cart) {
//        ShoppingCart existOne = getExist(cart);
//        if (existOne == null) {
//            cart.setNumber(1);
//            cart.setCreateTime(LocalDateTime.now());
//            return save(cart);
//        } else {
//            return shoppingCartMapper.increaseNumber(existOne);
//        }

        boolean updated;
        // 直接更新数据库中购物车项的数量
        if (cart.getDishId() != null) {
            updated = shoppingCartMapper.increaseNumberByUserIdAndDishId(cart);
        } else if (cart.getSetmealId() != null) {
            updated = shoppingCartMapper.increaseNumberByUserIdAndSetMealId(cart);
        } else {
            throw new ServiceException("未选择菜品或套餐");
        }

        // 若更新失败，说明未存在该项目，则新建
        if (!updated) {
            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());
            return save(cart);
        }

        return true;
    }

    @Transactional
    @Override
    public boolean subtractItem(ShoppingCart cart) {
//        ShoppingCart existOne = getExist(cart);
//        if (existOne != null && existOne.getNumber() > 0) {
//            return shoppingCartMapper.decreaseNumber(existOne.getId());
//        }
//        return false;

        if (cart.getDishId() != null) {
            return shoppingCartMapper.decreaseNumberByUserIdAndDishId(cart);
        } else if (cart.getSetmealId() != null) {
            return shoppingCartMapper.decreaseNumberByUserIdAndSetMealId(cart);
        } else {
            throw new ServiceException("未选择菜品或套餐");
        }
    }

    @Override
    public void clean(long id) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, id);

        remove(queryWrapper);
    }

    @Override
    public BigDecimal getAmount(long uid) {
        return shoppingCartMapper.getAmount(uid);
    }
}
