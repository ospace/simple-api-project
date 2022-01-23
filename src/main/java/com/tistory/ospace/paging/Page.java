package com.tistory.ospace.paging;

import java.util.ArrayList;
import java.util.List;

public class Page<T> extends ArrayList<T> {
	private static final long serialVersionUID = -150724166123661498L;
	private Long total;
	
	public Page(Long total, List<T> data) {
		super(data);
		this.total  = total;
	}

	public Long getTotal() {
		return total;
	}
}
