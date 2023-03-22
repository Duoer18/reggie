package com.duoer.takeout.dto;

import com.duoer.takeout.entity.Setmeal;
import com.duoer.takeout.entity.SetmealDish;
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
