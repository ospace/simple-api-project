package com.tistory.ospace.api.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.tistory.ospace.api.controller.model.SearchKeyword;
import com.tistory.ospace.api.repository.dto.AuthDto;
import com.tistory.ospace.api.repository.dto.UserDto;
import com.tistory.ospace.common.BaseDto;
import com.tistory.ospace.paging.base.PagingUtils;


public class BaseUtils {
	
	public static String getPrincipal() {
		if(SecurityContextHolder.getContext().getAuthentication() == null){
			return "GUS";
		}
		
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (obj instanceof UserDetails) {
            return ((UserDetails) obj).getUsername();
        } else {
            return "GUS";
        }
    }
	
	/**
	 * 유저 정보
	 * @return
	 */
	public static UserDto getUser(){
		
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(obj.equals("anonymousUser")){
			return null;
		}else{
			//암호화 전 패스워드
			//((AuthInfo)obj).getUser().setPassword(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
			return ((AuthDto)obj).getUser();
		}
		
	}
	
	public static int getUserId() {
		UserDto account = getUser();
		return null == account ? -1 : account.getId();
	}

	public static void applyCreator(BaseDto dto) {
		if (null == dto) return;
		
		Integer creator = getUserId();
		dto.setCreator(creator);
	}

	public static void offsetPage(SearchKeyword searchKeyword) {
		PagingUtils.setRowBounds(searchKeyword.getOffset(), searchKeyword.getLimit());
	}
}
