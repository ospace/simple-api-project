package com.tistory.ospace.api.repository.dto;

import org.apache.ibatis.type.Alias;

import com.tistory.ospace.common.BaseDto;

@Alias("File")
public class FileDto extends BaseDto {
	private String  id;
	private String  orgFilename;
	private String  filepath;
	private String  extension;
	private String  type;
	private Long    size;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgFilename() {
		return orgFilename;
	}
	public void setOrgFilename(String orgFilename) {
		this.orgFilename = orgFilename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append("/file/")
			.append(getFilepath())
			.append("/")
			.append(getId())
			.append(".")
			.append(getExtension());
		
		return sb.toString();
	}
}
