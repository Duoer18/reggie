package com.duoer.reggie.dto;

import com.duoer.reggie.entity.SetmealDish;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SetmealDishDto extends SetmealDish {
    private String image;
}
