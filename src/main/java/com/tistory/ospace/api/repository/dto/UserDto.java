package com.tistory.ospace.api.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.api.util.YN;
import com.tistory.ospace.common.BaseDto;

@Alias("User")
public class UserDto extends BaseDto {
	private Integer id;
	private String loginId;
	private String name;
	private String password;
	private YN useYn;
	private String imageId;
	private String roleCode;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public YN getUseYn() {
		return useYn;
	}
	public void setUseYn(YN useYn) {
		this.useYn = useYn;
	}
	public Boolean isUse() {
		return YN.Y == this.useYn;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
}