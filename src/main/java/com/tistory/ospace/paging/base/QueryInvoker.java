package com.tistory.ospace.paging.base;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

public class QueryInvoker {
	private Executor executor;
	private MappedStatement mappedStatement;
	private Map<String, Object> parameter;
	private ResultHandler<?> resultHandler;
	private BoundSql boundSql;
	
	@SuppressWarnings("unchecked")
	public QueryInvoker(Invocation invocation) {
		this.executor = (Executor) invocation.getTarget();
		Object[] args = invocation.getArgs();
		this.mappedStatement = (MappedStatement) args[0];
		this.parameter = (Map<String, Object>) args[1];
		this.resultHandler = (ResultHandler<?>) args[3];
		
		if(4 == args.length) {
			this.boundSql = mappedStatement.getBoundSql(this.parameter);
		} else {
			this.boundSql = (BoundSql) args[5];
		}
	}
	
	public MappedStatement getMappedStatement() {
		return mappedStatement;
	}
	
	public BoundSql getBoundSql() {
		return boundSql;
	}
	
	public Map<String, Object> getParameter() {
		return parameter;
	}
	
	public String getQueryId() {
		return mappedStatement.getId();
	}
	
	public String getSql() {
		return boundSql.getSql();
	}
	
	public CacheKey newCacheKey(MappedStatement ms, BoundSql bs) {
		return executor.createCacheKey(ms, this.parameter, RowBounds.DEFAULT, bs);
	}
	
	public List<Object> invoke(MappedStatement ms, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
		return executor.query(ms, this.parameter, RowBounds.DEFAULT, this.resultHandler, cacheKey, boundSql);
	}
}
