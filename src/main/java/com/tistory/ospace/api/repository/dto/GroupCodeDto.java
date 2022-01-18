package com.tistory.ospace.api.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.common.BaseDto;

@Alias("GroupCode")
public class GroupCodeDto extends BaseDto {
	private String        code;
	private String        name;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
