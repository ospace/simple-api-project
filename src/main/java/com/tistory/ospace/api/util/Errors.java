package com.tistory.ospace.api.util;

import java.util.Map;

import com.tistory.ospace.common.DataUtils;

public enum Errors {
	// 1 ~ 1000 : reserved
	DATA_INTEGRITY(1010, "데이터 무결성 깨짐"),
	DUPLICATE(1011, "데이터 중복"),
	INVALID_PARAM(1012, "잘못된 매개변수"),
	;
	
	public final int status;
	public final String message;
	
	private Errors(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public static Errors find(int status) {
		for(Errors each : Errors.values())
			if(each.status == status) return each;
		return null;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public boolean equalStatus(Integer status) {
		return null != status && this.status == status;
	}
	
	public static Map<Integer, String> toMap() {
		return DataUtils.map(values(), it->it.status, it->it.message);
	}
}
