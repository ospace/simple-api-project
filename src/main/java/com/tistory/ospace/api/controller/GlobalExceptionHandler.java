package com.tistory.ospace.api.controller;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tistory.ospace.api.exception.BaseException;
import com.tistory.ospace.api.exception.DataIntegrityException;
import com.tistory.ospace.api.exception.DuplicateException;
import com.tistory.ospace.api.util.Errors;
import com.tistory.ospace.common.CmmUtils;
import com.tistory.ospace.common.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public Response<?> handleBaseException(Exception ex) {
		logger.error("서버내부 오류: {}", ex.getMessage(), ex);
		return Response.fail(1, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({FileNotFoundException.class})
	public void handleFileNotFound(FileNotFoundException e, HttpServletRequest req, Model model) {
		logger.error("{}: uri[{}] message[{}]", e.getClass().getSimpleName(), req.getRequestURI(), e.getMessage());
		
		//model.addAttribute("path", req.getRequestURI());
		//model.addAttribute("message", e.getMessage());
		
		//return "forward:/error/404";
		//return null;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = BaseException.class)
    public Response<?> handleBaseException(BaseException ex) {
	    String message = null;
	    if(ex instanceof DuplicateException) {
	        message = ex.getMessage();
	        logger.error("중복 오류: {}", message, ex);    
	    } else if (ex instanceof DataIntegrityException){
	        String source = "다른 곳";
	        
	        if(null == ex.getCause()) {
	            message = ex.getMessage();
	        } else {
	        	source = parseSource((DataIntegrityException)ex);
	        }
    	        
	        logger.error("데이터무결성 오류: error[{}]", message, ex);
	        message = source+"에서 사용중입니다";
	    }
        
        return Response.fail(ex.status, message);
    }
	
    /*
     * JSR 380: Controller에서 파리미터에 @Valid 추가 필요
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Response<?> handleValidationExceptions(HttpServletRequest req, BindException ex) {
        logger.error("유효성에러: path[{}]", req.getRequestURI(), ex);
        
        return Response.of(HttpStatus.BAD_REQUEST.value(), "유효성 에러", CmmUtils.toString(ex.getBindingResult()));
    }
    
	@ExceptionHandler(ConstraintViolationException.class)
	public Response<?> handleConstraintViolationExceptions(HttpServletRequest req, ConstraintViolationException ex) {
		Errors err = Errors.INVALID_PARAM;
		String msg = ex.getMessage();
		logger.error("요청파리미터 오류: status[{}] path[{}] message[{}]", err.status, req.getRequestURI(), msg, ex);
		
	    return Response.of(err.status, err.message, msg);
	}
    
	@SuppressWarnings("serial")
	private static final Map<String, String> DB_TABLE_MAP = new HashMap<String, String>() {{
		put("`tb_user`", "사용자");
	}};
	
	static private final Pattern CONSTRAINT_PTN = Pattern.compile(
		"constraint fails \\(([\\w`\\.]+), .* FOREIGN KEY \\(([\\w`]+)\\)",
		Pattern.MULTILINE
	);
	
	private static String parseSource(DataIntegrityException ex) {
		String message = ex.getCause().getMessage();
        Matcher matcher = CONSTRAINT_PTN.matcher(message);
        String ret = "others";
        if(matcher.find()) {
            String group = matcher.group(1);
            String splited[] = group.split("\\.");
            String tableName = splited[splited.length-1];
            ret = DB_TABLE_MAP.get(tableName);
            if(null == ret) ret = tableName;
        }
        
		return ret;
	}
}
