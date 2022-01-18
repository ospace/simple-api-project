package com.tistory.ospace.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Response<T> {
	private int    status = 0;
	private String message;
	private T      response;
	
	public static <T> Response<T> fail(int status, String message) {
		return of(status, message, null);
	}
	
	public static <T> Response<T> success(T response) {
		return of(0, null, response);
	}
	
	public static <T> Response<T> of(int status, String message, T response) {
		Response<T> ret = new Response<T>();
		ret.setStatus(status);
		ret.setMessage(message);
		ret.setResponse(response);
		return ret;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}
}
