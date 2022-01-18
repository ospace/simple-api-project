package com.tistory.ospace.api.util;

import java.util.Map;

import com.tistory.ospace.common.DataUtils;

public enum YN {

	Y("YES", "사용"), N("NO", "미사용");
	
	public final String value;
	public final String name;
	
	private YN(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public static YN find(String value) {
		for(YN each : YN.values())
			if(each.value.equals(value)) return each;
		return null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public boolean equalValue(String value) {
		return this.value.equals(value);
	}
	
	public static Map<String,String> toMap() {
		return DataUtils.map(values(), it->it.value, it->it.name);
	}
	
	public static Boolean toBoolean(YN value) {
		if(null == value) return null;
		switch(value) {
		case Y: return true;
		default: return false;
		}
	}
	
	public static YN toYn(Boolean value) {
		if(null == value) return null;
		return value ? Y : N;
	}
}
