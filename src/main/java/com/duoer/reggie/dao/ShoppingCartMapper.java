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
    @Update("update shopping_cart set number = number + 1 " +
            "where id = #{id}")
    boolean increaseNumber(ShoppingCart cart);
    @Update("update shopping_cart set number = number - 1 where id = #{id} and number > 0")
    boolean decreaseNumber(@Param("id") long id);

    @Select("select sum(amount * number) from shopping_cart where user_id = #{uid}")
    BigDecimal getAmount(@Param("uid") long uid);
}
