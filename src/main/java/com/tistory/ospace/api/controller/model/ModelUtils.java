package com.tistory.ospace.api.controller.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tistory.ospace.api.repository.dto.CodeDto;
import com.tistory.ospace.api.repository.dto.FileDto;
import com.tistory.ospace.api.repository.dto.SearchDto;
import com.tistory.ospace.api.repository.dto.UserDto;
import com.tistory.ospace.api.service.UserService;
import com.tistory.ospace.api.util.YN;
import com.tistory.ospace.common.BaseDto;
import com.tistory.ospace.common.CmmUtils;
import com.tistory.ospace.common.DataUtils;

public class ModelUtils {
	public static Map<String, CodeDto> convertMap(List<CodeDto> data) {
		return DataUtils.map(data, it->it.getCode(), it->it);
	}
	
	public static <T extends BaseDto> void mappingUserName(UserService userService, List<T> data) {
		List<UserDto> res = userService.searchIn(mapId(data));
		
		if(DataUtils.isEmpty(res)) return;
		
		Map<Integer, UserDto> userMap = DataUtils.map(res, it->it.getId(), it->it);
		mappingUserName(userMap, data);
	}
	
	private static <T extends BaseDto> void mappingUserName(Map<Integer, UserDto> userMap, List<T> data) {
		DataUtils.iterate(data, it->{
			UserDto user = null;
			if(null != it.getModifier()) {
				user = userMap.get(it.getModifier());
				if(null != user) it.setModifierName(user.getLoginId());
			}
			
			if(null != it.getCreator()) {
				user = userMap.get(it.getCreator());
				if(null != user) it.setCreatorName(user.getLoginId());
			}
		});
	}
	
	private static <T extends BaseDto> Set<Integer> mapId(List<T> data) {
		return DataUtils.reduce(data, (n,it)->{
			if(null!=it.getModifier()) n.add(it.getModifier());
			if(null!=it.getCreator()) n.add(it.getCreator());
		}, new HashSet<Integer>());
	}
	
	public static SearchDto convert(SearchKeyword from, SearchDto to) {
		if(null == from) return to;
		
		CmmUtils.convert(from, to);
		to.setType(from.getSearchType());
		to.setKeyword(from.getSearchKeyword());
		
		return to;
	}
	
//	public static FileInfo convert(FileDto from, FileInfo to) {
//		if(null == from) return to;
//		
//		CmmUtils.convert(from, to, "filename");
//		to.setUrl(createFileUrl(from.getFilename()));
//		
//		return to;
//	}

//	private static String createFileUrl(String filename) {
//	    if(StrUtils.isEmpty(filename)) return null;
//	    
//		StringBuilder sb = new StringBuilder();
//		sb.append("/file");
//		char c = filename.charAt(0);
//		if ('/' != c && '\\' != c) {
//			sb.append('/');
//		}
//		sb.append(filename);
//		
//		return sb.toString();
//	}

	public static User convert(UserDto from, User to) {
		if(null == from) return to;
		
		CmmUtils.convert(from, to, "password");
		to.setEnable(YN.toBoolean(from.getUseYn()));
		
		return to;
	}

	public static UserDto convert(User from, UserDto to) {
		if(null == from) return to;
		
		CmmUtils.convert(from, to);
		to.setUseYn(YN.toYn(from.getEnable()));
		
		return to;
	}

	public static FileRS convert(FileDto from, FileRS to) {
		if(null == from) return to;
		
		CmmUtils.convert(from, to);
		
		return to;
	}
}
