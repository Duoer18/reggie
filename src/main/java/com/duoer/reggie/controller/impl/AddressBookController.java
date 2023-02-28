package com.duoer.reggie.controller.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.duoer.reggie.common.BaseContext;
import com.duoer.reggie.common.Result;
import com.duoer.reggie.entity.AddressBook;
import com.duoer.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public Result save(@RequestBody AddressBook a) {
        log.info("add {}", a);

        a.setUserId(BaseContext.getUId());
        boolean isSaved = addressBookService.save(a);
        if (isSaved) {
            return Result.success(a);
        } else {
            return Result.failed("地址添加失败");
        }
    }

    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook a) {
        log.info("set default addressBook:{}", a);

        // 确保用户只能设置自己的地址
        Long uId = BaseContext.getUId();
        AddressBook address = addressBookService.getById(a.getId());
        if (address == null || address.getUserId() == null || !address.getUserId().equals(uId)) {
            return Result.failed("invalid access");
        }

        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, uId);
        wrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(wrapper);

        a.setIsDefault(1);
        addressBookService.updateById(a);
        return Result.success(a);
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        log.info("get address id={}", id);

        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            // 确保用户只能获取自己的地址
            if (addressBook.getUserId() != null && addressBook.getUserId().equals(BaseContext.getUId())) {
                return Result.success(addressBook);
            } else {
                return Result.failed("invalid access");
            }
        } else {
            return Result.failed("没有找到该对象");
        }
    }

    @GetMapping("/default")
    public Result getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getUId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return Result.failed("没有找到该对象");
        } else {
            return Result.success(addressBook);
        }
    }

    @PutMapping
    public Result setAddress(@RequestBody AddressBook a) {
        log.info("update {}", a);

        Long eId = BaseContext.getUId();
        AddressBook address = addressBookService.getById(a.getId());
        if (address == null || address.getUserId() == null || !address.getUserId().equals(eId)) {
            return Result.failed("invalid access");
        }

        a.setIsDefault(null);
        a.setCreateTime(null);
        a.setCreateUser(null);
        addressBookService.updateById(a);
        return Result.success(a);
    }

    @DeleteMapping
    public Result deleteAddress(@RequestParam List<Long> ids) {
        log.info("delete ids={}", ids);

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AddressBook::getId, ids)
                .eq(AddressBook::getUserId, BaseContext.getUId());

        boolean isRemoved = addressBookService.remove(queryWrapper);
        if (isRemoved) {
            return Result.success("地址删除成功");
        } else {
            return Result.failed("地址删除失败");
        }
    }

    @GetMapping("/list")
    public Result list(AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);

        addressBook.setUserId(BaseContext.getUId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        return Result.success(addressBookService.list(queryWrapper));
    }
}
