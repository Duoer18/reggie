package com.duoer.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duoer.reggie.entity.ShoppingCart;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    List<ShoppingCart> listCarts(long uid);
    boolean addItem(ShoppingCart cart);

    @Transactional
    boolean subtractItem(ShoppingCart cart);

    void clean(long id);

    BigDecimal getAmount(long uid);
}
