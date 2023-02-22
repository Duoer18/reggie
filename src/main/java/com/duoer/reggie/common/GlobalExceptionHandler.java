package com.duoer.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        String exMsg = ex.getMessage();
        log.info(exMsg);
        if (exMsg.contains("Duplicate entry")) {
            return Result.failed(exMsg.split(" ")[2] + "已存在");
        }
        return Result.failed("参数输入有误");
    }

    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException ex) {
        return Result.failed(ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public Result handleIOException(IOException ex) {
        return Result.failed("文件传输操作异常");
    }
}
