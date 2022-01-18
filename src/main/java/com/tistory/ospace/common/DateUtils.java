package com.tistory.ospace.common;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final String EMPTY = "";
	private static DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static DateTimeFormatter fmtDateShort = DateTimeFormatter.ofPattern("MM-dd");
	private static DateTimeFormatter fmtDateShort2 = DateTimeFormatter.ofPattern("yyyyMM");
	
	private static DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static DateTimeFormatter fmtTimeShort = DateTimeFormatter.ofPattern("HH:mm");
	
	public static LocalDateTime now() {
		return LocalDateTime.now();
	}
	
	public static String toStringDateTime(LocalDateTime date) {
		if(null == date) return EMPTY;
		return date.format(fmtDateTime);
	}
	
	public static String toStringDate(LocalDateTime date) {
		if(null == date) return EMPTY;
		return date.format(fmtDate);
	}
	
	public static String toStringTimeShort(LocalTime date) {
		if(null == date) return EMPTY;
		return date.format(fmtTimeShort);
	}
	
	public static String toStringTime(LocalTime date) {
		if(null == date) return EMPTY;
		return date.format(fmtTime);
	}
	
	public static String toStringDateShort(LocalDateTime date) {
		if(null == date) return EMPTY;
		return date.format(fmtDateShort);
	}
	
	public static String toStringDateShort2(LocalDateTime date) {
		if(null == date) return EMPTY;
		return date.format(fmtDateShort2);
	}
}
