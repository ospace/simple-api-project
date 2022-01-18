package com.tistory.ospace.api.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.common.BaseDto;

@Alias("UserLog")
public class UserLogDto extends BaseDto {
	private Integer userId;
	private String remoteIp;
	private Integer status;
	private String message;
	private String userAgent;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
