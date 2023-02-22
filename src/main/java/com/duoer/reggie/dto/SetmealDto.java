package com.duoer.reggie.dto;

import com.duoer.reggie.entity.Setmeal;
import com.duoer.reggie.entity.SetmealDish;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SetmealDto extends Setmeal {
    private String categoryName;
    private List<SetmealDish> setmealDishes;
}
