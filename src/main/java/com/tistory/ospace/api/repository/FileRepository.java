package com.tistory.ospace.api.repository;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tistory.ospace.api.repository.dto.FileDto;
import com.tistory.ospace.api.repository.dto.SearchDto;

@Mapper
public interface FileRepository {

	Integer countBy(@Param("search") SearchDto search);
	
	List<FileDto> findAllBy(@Param("search") SearchDto search);

	FileDto findById(String id);
	
	FileDto findBy(FileDto file);
	
	void insert(FileDto fileInfo);

	void deleteById(String id);

	List<FileDto> findAllIn(@Param("ids") Collection<String> ids);

}
