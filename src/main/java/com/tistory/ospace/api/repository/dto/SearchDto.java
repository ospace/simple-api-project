package com.tistory.ospace.api.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.common.BaseData;

@Alias("Search")
public class SearchDto extends BaseData {
	private String type;
	private String keyword;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
