package com.duoer.reggie.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.duoer.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    @Update("update shopping_cart set number = number + 1 where id = #{id}")
    boolean increaseNumber(ShoppingCart cart);

    @Update("update shopping_cart set number = number + 1 where user_id = #{userId} and dish_id = #{dishId}")
    boolean increaseNumberByUserIdAndDishId(ShoppingCart cart);

    @Update("update shopping_cart set number = number + 1 " +
            "where user_id = #{userId} and setmeal_id = #{setmealId}")
    boolean increaseNumberByUserIdAndSetMealId(ShoppingCart cart);

    @Update("update shopping_cart set number = number - 1 where id = #{id} and number > 0")
    boolean decreaseNumber(@Param("id") long id);

    @Update("update shopping_cart set number = number - 1 " +
            "where user_id = #{userId} and dish_id = #{dishId} and number > 0")
    boolean decreaseNumberByUserIdAndDishId(ShoppingCart cart);

    @Update("update shopping_cart set number = number - 1 " +
            "where user_id = #{userId} and setmeal_id = #{setmealId} and number > 0")
    boolean decreaseNumberByUserIdAndSetMealId(ShoppingCart cart);

    @Select("select sum(amount * number) from shopping_cart where user_id = #{uid}")
    BigDecimal getAmount(@Param("uid") long uid);
}
