package com.tistory.ospace.api.repository;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tistory.ospace.api.repository.dto.SearchDto;
import com.tistory.ospace.api.repository.dto.UserDto;

@Mapper
public interface UserRepository {

	int countBy(@Param("search") SearchDto search);

	List<UserDto> findAllBy(@Param("search") SearchDto search);

	List<UserDto> findAllIn(@Param("ids") Collection<Integer> ids);

	UserDto findById(Integer id);
	
	UserDto findByLoginId(String loginId);

	void insert(UserDto user);

	void update(UserDto user);

	void deletetById(Integer id);
}
