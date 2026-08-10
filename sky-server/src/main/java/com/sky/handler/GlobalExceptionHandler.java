package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.exception.NotAdminException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error("异常信息：{}",ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String tmp = ex.getMessage();
            String username = tmp.split(" ")[2];

            String msg = MessageConstant.USERNAME_EXIST + username;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

    @ExceptionHandler(NotAdminException.class)
    public Result exceptionHandler(NotAdminException ex){
        log.error("异常信息：{}",ex.getMessage());

        return Result.error(ex.getMessage());
    }
}
