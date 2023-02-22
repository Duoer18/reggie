package com.duoer.reggie.dto;

import com.duoer.reggie.entity.Dish;
import com.duoer.reggie.entity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishDto extends Dish {
    private List<DishFlavor> flavors;
    private String categoryName;
    private Integer copies;
}
