package com.tistory.ospace.paging;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.plugin.Invocation;

import com.tistory.ospace.paging.base.PagingUtils;
import com.tistory.ospace.paging.base.QueryInvoker;
import com.tistory.ospace.paging.query.DefaultQueryCount;
import com.tistory.ospace.paging.query.DefaultQueryList;
import com.tistory.ospace.paging.query.QueryTemplate;

public class PagingInvoker {
	private QueryTemplate queryCount;
	private QueryTemplate queryList;
	
	public PagingInvoker() {
		this.queryCount = new DefaultQueryCount();
		this.queryList = new DefaultQueryList();
	}
	
	public boolean isPaging() {
		return null != PagingUtils.getRowBounds();
	}

	public Object proceed(Invocation invocation) throws SQLException {
		
		QueryInvoker queryInvoker = new QueryInvoker(invocation); 
		
		List<Object> res = this.queryCount.execute(queryInvoker);
		Long total = (Long)res.get(0);
		
		List<Object> data = this.queryList.execute(queryInvoker);
		
		return new Page<>(total, data);
	}
}
