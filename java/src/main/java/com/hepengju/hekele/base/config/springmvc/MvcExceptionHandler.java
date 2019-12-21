package com.hepengju.hekele.base.config.springmvc;

import com.hepengju.hekele.base.core.HeException;
import com.hepengju.hekele.base.core.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局的异常处理器
 */
@ControllerAdvice
public class MvcExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(HeException.class)
    @ResponseBody
    public R handleException(HeException e) {
        return R.err(e.getMessage());
    }

    /**
     * 其他所有异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R handleException(Exception e) {
        return R.err(e.getMessage());
    }
}
