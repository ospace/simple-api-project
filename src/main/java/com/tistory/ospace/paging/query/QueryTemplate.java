package com.tistory.ospace.paging.query;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import com.tistory.ospace.paging.base.QueryInvoker;

public abstract class QueryTemplate {

	public List<Object> execute(QueryInvoker queryInvoker) throws SQLException {
		MappedStatement ms = onMappedStateMent(queryInvoker.getMappedStatement());
		BoundSql bs = onBoundSql(ms, queryInvoker.getBoundSql(), queryInvoker.getParameter());
		CacheKey ck = queryInvoker.newCacheKey(ms, bs);
		
		return queryInvoker.invoke(ms, ck, bs);
	}

	protected abstract MappedStatement onMappedStateMent(MappedStatement ms);
	
	protected abstract BoundSql onBoundSql(MappedStatement ms, BoundSql bs, Map<String, Object> param);
}
