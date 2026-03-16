package com.bite.forum.exception;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public AppResult applicationException(ApplicationException e){
        e.printStackTrace();
        log.error(e.getMessage());
        if(e.getErrorResult() != null){
            return e.getErrorResult();
        }
        if(e.getMessage() == null || e.getMessage().equals("")){
            return AppResult.failed(ResultCode.ERROR_SERVICES);
        }
        return AppResult.failed(e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public AppResult handleException (Exception e) {
        // 打印异常
        e.printStackTrace();
        // 记录⽇志
        log.error(e.getMessage());
        if (e.getMessage() == null) {
            return AppResult.failed(ResultCode.ERROR_SERVICES);
        }
        // 默认返回异常信息
        return AppResult.failed(e.getMessage());
    }
}
