package com.tistory.ospace.api.exception;

import com.tistory.ospace.api.util.Errors;

/*
 * 무결성 오류가 발생: 삭제, 수정시 다른 곳에서 참조 중인 경우 발생
 */
public class DataIntegrityException extends BaseException {
	private static final long serialVersionUID = 3177686619890627943L;
	public static final Errors ERROR = Errors.DATA_INTEGRITY;

	public DataIntegrityException() {
		super(ERROR);
	}
	
	public DataIntegrityException(String msg) {
		super(ERROR, msg);
	}
	
	public DataIntegrityException(Exception e) {
		super(ERROR, e);
	}
	
	public DataIntegrityException(String msg, Exception e) {
		super(ERROR, msg, e);
	}
}
