package com.hepengju.hekele.base.core.exception;

/**
 * HibernateValidator验证异常
 *
 * @author he_pe 2019-12-23
 */
public class BeanValidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BeanValidException(String message) {
        super(message);
    }
}
