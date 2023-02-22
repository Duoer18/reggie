package com.duoer.reggie.common;

public class BaseContext {
    private static final ThreadLocal<Long> eidLocal = new ThreadLocal<>();
    private static final ThreadLocal<Long> uidLocal = new ThreadLocal<>();

    public static void setEid(Long id) {
        eidLocal.set(id);
    }

    public static Long getEId() {
        return eidLocal.get();
    }

    public static void setUid(Long id) {
        uidLocal.set(id);
    }

    public static Long getUId() {
        return uidLocal.get();
    }
}
