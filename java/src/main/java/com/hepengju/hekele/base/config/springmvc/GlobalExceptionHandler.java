package com.hepengju.hekele.base.config.springmvc;

import com.hepengju.hekele.base.core.R;
import com.hepengju.hekele.base.core.exception.BeanValidException;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.base.util.ValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;

/**
 * 全局的异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 自动的数据绑定验证异常拦截
     *
     * <pre>
     * 接口上的Bean验证，需要在参数前加上@Valid或Spring的 @Validated注解，这两种注释都会导致应用标准Bean验证。
     * 如果验证不通过会抛出BindException异常，并变成400（BAD_REQUEST）响应；
     * 或者可以通过Errors或BindingResult参数在控制器内本地处理验证错误。
     * 另外，如果参数前有@RequestBody注解，验证错误会抛出MethodArgumentNotValidException异常。
     * </pre>
     */
    @ExceptionHandler(BindException.class)
    public R handleBindException(BindException e) {
        ObjectError objectError = e.getAllErrors().get(0);
        return R.err(getObjectErrorMessage(objectError)).setCode(10001);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleBindException(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return R.err(getObjectErrorMessage(objectError)).setCode(10002);
    }

    private String getObjectErrorMessage(ObjectError objectError) {
        try {
            ConstraintViolation violation = objectError.unwrap(ConstraintViolation.class);
            String errorMessage = ValidUtil.getErrorMessage(violation);
            return errorMessage;
        } catch (Exception e) {
            return objectError.getObjectName() + objectError.getDefaultMessage();
        }
    }

    /**
     * 手动的数据绑定验证异常拦截
     */
    @ExceptionHandler(BeanValidException.class)
    public R handleBeanValidException(BeanValidException e) {
        log.error(e.getMessage(), e);
        return R.err(e.getMessage()).setCode(10003);
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(HeException.class)
    public R handleHeException(HeException e) {
        // TODO 特殊处理
        log.error(e.getMessage(), e);
        return R.err(e.getMessage());
    }

    /**
     * 其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.err(e.getMessage()).setCode(-1);
    }
}
