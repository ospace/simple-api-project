package com.tistory.ospace.api.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tistory.ospace.api.controller.model.SearchKeyword;
import com.tistory.ospace.api.repository.dto.AuthDto;
import com.tistory.ospace.api.repository.dto.UserDto;
import com.tistory.ospace.common.BaseDto;
import com.tistory.ospace.paging.base.PagingUtils;

public class SpringUtils {
	private static final ObjectMapper jsonSimpleObjectMapper = new ObjectMapper()
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
			.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
			.registerModule(new JavaTimeModule())
			.setSerializationInclusion(Include.NON_NULL);
	
	private static final MappingJackson2HttpMessageConverter httpConverter = new MappingJackson2HttpMessageConverter(jsonSimpleObjectMapper);
	
	public static <T> void writeJson(T from, HttpServletResponse to) throws HttpMessageNotWritableException, IOException {
		httpConverter.write(from, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(to));
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
	
	public static Integer getUserId() {
		UserDto account = getUser();
		return null == account ? null : account.getId();
	}

	public static void offsetPage(SearchKeyword searchKeyword) {
		PagingUtils.setRowBounds(searchKeyword.getOffset(), searchKeyword.getLimit());
	}
	
	public static void applyUser(BaseDto dto) {
		if (null == dto) return;
		applyUser(dto, getUserId());
	}
	
	public static void applyUser(BaseDto dto, Integer userId) {
		dto.setModifier(userId);
		dto.setCreator(userId);
	}
}
