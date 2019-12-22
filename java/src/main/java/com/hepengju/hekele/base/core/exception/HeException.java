package com.hepengju.hekele.base.core.exception;

import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class HeException extends RuntimeException {

    private Object[] errFormatArr;

    public HeException(String message, Object... obj) {
        super(message);
        this.errFormatArr = obj;
    }

    //由于实际需要,因此又追加以下两种构造方法
    public HeException(String message, Throwable cause) {
        super(message, cause);
    }
    public HeException(Throwable cause) {
        super(cause);
    }

}
