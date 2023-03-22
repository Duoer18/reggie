package com.duoer.takeout.dto;

import com.duoer.takeout.entity.SetmealDish;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SetmealDishDto extends SetmealDish {
    private String image;
}
