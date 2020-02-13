package com.hepengju.hekele.base.core.exception;

import java.util.List;

/**
 * Excel检查异常
 *
 * @author he_pe 2019-12-23
 */
public class ExcelCheckException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> errList;

    public ExcelCheckException(List<String> errList) {
        this.errList = errList;
    }

    public List<String> getErrList() {
        return errList;
    }

    public void setErrList(List<String> errList) {
        this.errList = errList;
    }
}
