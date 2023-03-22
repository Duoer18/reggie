package com.duoer.takeout.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    public static <T> T convertBean(Object source, Class<? extends T> tagerClass, String... ignoreProperties) {
        T target = null;
        try {
            target = tagerClass.newInstance();
            BeanUtils.copyProperties(source, target, ignoreProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public static <T> T convertBean(Object source, Class<? extends T> tagerClass) {
        T target = null;
        try {
            target = tagerClass.newInstance();
            BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public static <T> List<? extends T> convertBeanList(List<?> source, Class<? extends T> targetClass) {
        return source.stream()
                .map(item -> {
                    try {
                        return convertBean(item, targetClass);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
