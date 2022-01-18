package com.tistory.ospace.api.exception;

import com.tistory.ospace.api.util.Errors;

/*
 * 중복에러로 기존에 데이터가 있는 경우 발생 
 */
public class DuplicateException extends BaseException {
	private static final long serialVersionUID = 3243208000567822765L;
	public static final Errors ERROR = Errors.DUPLICATE;
	
	public DuplicateException() {
		super(ERROR);
	}
	
	public DuplicateException(String msg) {
		super(ERROR, msg);
	}
	
	public DuplicateException(Exception e) {
		super(ERROR, e);
	}
	
	public DuplicateException(String msg, Exception e) {
		super(ERROR, msg, e);
	}
}
