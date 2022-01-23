package com.tistory.ospace.paging.configuration;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.tistory.ospace.paging.PagingInvoker;

@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }), })
public class QueryInterceptor implements Interceptor {
	
	private static PagingInvoker pagingTemplate = new PagingInvoker();
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (pagingTemplate.isPaging()) {
			return pagingTemplate.proceed(invocation);
		} else {
			return invocation.proceed();
		}
	}
}
