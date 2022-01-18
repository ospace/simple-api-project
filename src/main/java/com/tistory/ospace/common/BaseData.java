package com.tistory.ospace.common;

public class BaseData implements Cloneable {
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return CmmUtils.toJsonString(this);
	}
}
