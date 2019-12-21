package com.hepengju.hekele.base.config.springmvc;

import com.hepengju.hekele.base.core.HeException;
import com.hepengju.hekele.base.core.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局的异常处理器
 */
@ControllerAdvice @Slf4j
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
        log.error(e.getMessage(), e);
        return R.err(e.getMessage());
    }
}
