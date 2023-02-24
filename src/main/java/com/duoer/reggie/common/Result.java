package com.duoer.reggie.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;

@Data
@AllArgsConstructor
@ToString
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int code;
    private Object data;
    private String msg;
    private HashMap<String, Object> map = new HashMap<>();

    public Result(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success(Object data) {
        return new Result(1, data);
    }

    public static Result failed(String msg) {
        return new Result(0, msg);
    }

    public void add(String key, Object value) {
        map.put(key, value);
    }
}

