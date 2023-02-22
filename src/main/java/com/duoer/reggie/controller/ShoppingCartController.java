package com.duoer.reggie.controller;

import com.duoer.reggie.common.BaseContext;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.ShoppingCart;
import com.duoer.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart cart) {
        log.info("add {}", cart);

        cart.setUserId(BaseContext.getEId());
        boolean isSaved = shoppingCartService.addItem(cart);
        if (isSaved) {
            return Result.success("添加购物车成功");
        } else {
            return Result.failed("添加购物车失败");
        }
    }

    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCart cart) {
        log.info("add {}", cart);

        cart.setUserId(BaseContext.getEId());
        boolean isSaved = shoppingCartService.subtractItem(cart);
        if (isSaved) {
            return Result.success("修改购物车成功");
        } else {
            return Result.failed("修改购物车失败");
        }
    }

    @GetMapping("/list")
    public Result getShoppingCarts() {
        log.info("get all shopping carts");

        List<ShoppingCart> shoppingCarts = shoppingCartService.listCarts(BaseContext.getEId());
        return Result.success(shoppingCarts);
    }

    @DeleteMapping("/clean")
    public Result cleanShoppingCarts() {
        log.info("clean shopping carts");

        shoppingCartService.clean(BaseContext.getEId());
        return Result.success("");
    }
}
