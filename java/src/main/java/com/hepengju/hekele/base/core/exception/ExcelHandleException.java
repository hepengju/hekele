package com.hepengju.hekele.base.core.exception;

/**
 * Excel读取写入过程中的异常
 *
 *  @author hepengju 2020-01-10
 */
public class ExcelHandleException extends HeException {
    public ExcelHandleException(Throwable cause) {
        super(cause);
    }
    public ExcelHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelHandleException(String message, Object... obj) {
        super(message);
        this.errFormatArr = obj;
    }
}
