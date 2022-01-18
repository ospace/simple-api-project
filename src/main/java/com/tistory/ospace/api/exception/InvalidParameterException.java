package com.tistory.ospace.api.exception;

import com.tistory.ospace.api.util.Errors;

/*
 * 잘못된 매개변수를 사용하는 경우 
 */
public class InvalidParameterException extends BaseException {
    private static final long serialVersionUID = -1460446467405460801L;
    public static final Errors ERROR = Errors.INVALID_PARAM;
	
    public InvalidParameterException() {
		super(ERROR);
	}
	
	public InvalidParameterException(String msg) {
		super(ERROR, msg);
	}
	
	public InvalidParameterException(Exception e) {
		super(ERROR, e);
	}
	
	public InvalidParameterException(String msg, Exception e) {
		super(ERROR, msg, e);
	}
}
