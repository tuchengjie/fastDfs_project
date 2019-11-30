package com.xuecheng.api.config;

import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 统一异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public QueryResponseResult runtimeError(RuntimeException e){
        //读取日志
        logger.error("catch RuntimeException: "+e.getMessage());
        CommonCode fail = CommonCode.FAIL;
        fail.setMessage(e.getMessage());
        return new QueryResponseResult(fail, null);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public QueryResponseResult error(Exception e){
        logger.error("catch Exception: "+e.getMessage());
        return new QueryResponseResult(CommonCode.FAIL, null);
    }

}
