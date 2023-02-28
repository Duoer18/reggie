package com.duoer.reggie.controller.impl;

import com.duoer.reggie.common.Result;
import com.duoer.reggie.controller.AbstractSetmealController;
import com.duoer.reggie.entity.Setmeal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/u-setmeal")
@Slf4j
public class UserSetmealController extends AbstractSetmealController {
    @GetMapping("/list")
    public Result getSetmealList(Setmeal setmeal) {
        return super.getSetmealList(setmeal);
    }
}
