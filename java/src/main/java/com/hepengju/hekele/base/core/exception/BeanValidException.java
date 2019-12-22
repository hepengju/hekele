package com.hepengju.hekele.base.core.exception;

/**
 * HibernateValidator验证异常
 */
public class BeanValidException extends RuntimeException {
    public BeanValidException(String message) {
        super(message);
    }
}
