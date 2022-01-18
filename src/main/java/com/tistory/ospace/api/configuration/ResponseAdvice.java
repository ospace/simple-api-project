package com.tistory.ospace.api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.tistory.ospace.common.Response;

/*
 * 응답데이터 wrapping 시킴
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseAdvice.class);
	
    @Override
        public boolean supports(MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {
    	LOGGER.debug("supports: parameterType[{}]", returnType.getParameterType().getCanonicalName());
        // Response인 경우와 문자열인 경우는 건너뜀.
    	Class<?> clazz = returnType.getParameterType();
        return !Response.class.equals(clazz)
        		&& !String.class.equals(clazz)
        		&& !ResponseEntity.class.equals(clazz);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
    	LOGGER.debug("beforeBodyWrite: body[{}]", body);
        return Response.success(body);
    }
}
