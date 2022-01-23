package com.tistory.ospace.common;

import java.util.ArrayList;
import java.util.Collection;

public class PageVo<T> extends ArrayList<T> {
	private static final long serialVersionUID = -8679681878150932440L;
	
	private Long total;
	
	public PageVo(Long total, Collection<T> c) {
        super(c);
        this.total = total;
    }
    
    public Long getTotal() {
        return this.total;
    }
}
