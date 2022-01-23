package com.tistory.ospace.paging.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.tistory.ospace.paging.Page;

public class PagingUtils {
	private static ThreadLocal<RowBounds> rowBounding = new ThreadLocal<>();
	
	public static void setRowBounds(Integer offset, Integer limit) {
		if (null == limit || 0 == limit) return;
		
		rowBounding.set(new RowBounds(null == offset ? 0 : offset, limit));
	}
	
	public static RowBounds getRowBounds() {
		return rowBounding.get();
	}
	
	public static void resetRowBounds() {
		rowBounding.set(null);
	}
	
	public static Long ofTotal(List<?> obj) {
		if (null == obj) return 0L;
		if (obj instanceof Page) {
			return ((Page<?>)obj).getTotal();
		} else {
			return (long) obj.size();
		}
	}
}
