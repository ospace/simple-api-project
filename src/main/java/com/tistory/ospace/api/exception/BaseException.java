package com.tistory.ospace.api.exception;

import com.tistory.ospace.api.util.Errors;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 2288443710582826194L;

    public final int status;
    
    public BaseException(int status, String message) {
        super(message);
        this.status = status;
    }
    
    public BaseException(int status, String message, Exception e) {
        super(message, e);
        this.status = status;
    }
    
    public BaseException(Errors error) {
        super(error.getMessage());
        this.status = error.getStatus();
    }
    
    public BaseException(Errors error, String extraMessage) {
        super(error.getMessage() + (null == extraMessage ? "" : extraMessage));
        this.status = error.getStatus();
    }
    
    public BaseException(Errors error, Exception e) {
    	super(error.getMessage(), e);
        this.status = error.getStatus();
    }
    
    public BaseException(Errors error, String extraMessage, Exception e) {
    	super(error.getMessage() + (null == extraMessage ? "" : extraMessage), e);
        this.status = error.getStatus();
    }
}
