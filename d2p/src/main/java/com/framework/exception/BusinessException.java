package com.framework.exception;

/**
 * 业务异常处理
 * @author Administrator
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}
}
