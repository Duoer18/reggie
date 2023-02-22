package com.duoer.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 元数据对象处理器，为公共属性统一赋值
 */
@Component
@Slf4j
public class MetaObjectImpl implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        metaObject.setValue("createTime", now);
        metaObject.setValue("updateTime", now);

        Long id = BaseContext.getEId();
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        metaObject.setValue("updateTime", now);

        Long id = BaseContext.getEId();
        metaObject.setValue("updateUser", id);
    }
}
