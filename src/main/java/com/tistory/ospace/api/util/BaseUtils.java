package com.tistory.ospace.api.util;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tistory.ospace.api.controller.model.SearchKeyword;
import com.tistory.ospace.api.repository.dto.AuthDto;
import com.tistory.ospace.api.repository.dto.UserDto;
import com.tistory.ospace.common.BaseDto;


public class BaseUtils {
	
    public static void offsetPage(SearchKeyword search) {
        Integer offset = search.getOffset();
        Integer limit = search.getLimit();
//        String orderBy = search.getOrderBy();
//        if (!StringUtils.isEmpty(orderBy)) {
//            PageHelper.orderBy(orderBy);
//        }
        PageHelper.offsetPage(null == offset ? 0 : offset, null == limit ? 0 : limit);
    }
    
    public static <T> long getTotal(List<T> data) {
        if (null == data) return 0;
        return data instanceof Page ? ((Page<T>)data).getTotal() : data.size();
    }
    
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
}
