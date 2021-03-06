package com.hepengju.hekele.base.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 *
 * @author he_pe 2019-12-23
 */
public class HeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	protected Object[] errFormatArr;

    public HeException(String message, Object... obj) {
        super(message);
        this.errFormatArr = obj;
    }

    //由于实际需要,因此又追加以下两种构造方法
    public HeException(Throwable cause) {
        super(cause);
    }
    public HeException(String message, Throwable cause) {
        super(message, cause);
    }

}
