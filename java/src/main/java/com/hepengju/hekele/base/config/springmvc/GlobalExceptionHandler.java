package com.hepengju.hekele.base.config.springmvc;

import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.base.core.M;
import com.hepengju.hekele.base.core.R;
import com.hepengju.hekele.base.core.exception.BeanValidException;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.base.util.StackUtil;
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
     * SpringMVC数据绑定异常(application/x-www-form-urlencoded)
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
        return R.err(getObjectErrorMessage(objectError)).setCode(HeConst.MCode.SPRINGMVC_FORM_BIND_ERROR);
    }

    /**
     * SpringMVC数据绑定异常(application/json)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleBindException(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return R.err(getObjectErrorMessage(objectError)).setCode(HeConst.MCode.SPRINGMVC_JSON_BIND_ERROR);
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
     * 手动执行的Hibernate Bean Validation验证异常
     */
    @ExceptionHandler(BeanValidException.class)
    public R handleBeanValidException(BeanValidException e) {
        log.error(e.getMessage(), e);
        return R.err(e.getMessage()).setCode(HeConst.MCode.HIBERNATE_BEAN_VALID_ERROR);
    }

    /**
     * 自定义异常
     *
     * 1. 记录错误异常
     * 2. 如果是浏览器端, 则加入错误详细信息(移动端不加入)
     */
    @ExceptionHandler(HeException.class)
    public R handleHeException(HeException e) {
        log.error(e.getMessage(), e);
        R result = M.getErrR(e.getMessage(), e.getErrFormatArr());
        // TODO 是否返回堆栈信息
        return true ? result.setExtra(StackUtil.getStackTrace(e)) : result;
    }

    /**
     * 其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.err(e.getMessage()).setCode(HeConst.MCode.UNKNOWN_ERROR);
    }
}
