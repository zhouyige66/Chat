package cn.kk20.chat.api.controller;

import cn.kk20.chat.base.exception.RequestParamException;
import cn.kk20.chat.base.http.ResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 统一异常处理
 * @Author: Roy Z
 * @Date: 2020/3/9 09:49
 * @Version: v1.0
 */
@ControllerAdvice
public class UnifiedExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultData handle(Exception ex){
        if(ex instanceof RequestParamException){
            return ResultData.requestError(ex.getMessage());
        }else {
            return ResultData.serverError(ex.getMessage());
        }
    }

}
