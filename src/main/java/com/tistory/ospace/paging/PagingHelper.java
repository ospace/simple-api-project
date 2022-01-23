package com.tistory.ospace.paging;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;

public class PagingHelper {
	public static String join(String[] strings) {
		return null == strings ? null : String.join(",", strings);
	}
	
	public static MappedStatement newMappedStatement(MappedStatement ms, String newId) {
		ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), newId, Long.class, new ArrayList<>(0))
				.build();
		
		return new MappedStatement.Builder(ms.getConfiguration(), newId, ms.getSqlSource(), ms.getSqlCommandType())
				.resource(ms.getResource())
				.parameterMap(ms.getParameterMap())
				.resultMaps(Arrays.asList(resultMap))
				.fetchSize(ms.getFetchSize())
				.timeout(ms.getTimeout())
				.statementType(ms.getStatementType())
				.resultSetType(ms.getResultSetType())
				.cache(ms.getCache())
				.flushCacheRequired(ms.isFlushCacheRequired())
				.useCache(true)
				.resultOrdered(ms.isResultOrdered())
				.keyGenerator(ms.getKeyGenerator())
				.keyColumn(PagingHelper.join(ms.getKeyColumns()))
				.keyProperty(PagingHelper.join(ms.getKeyProperties()))
				.databaseId(ms.getDatabaseId())
				.lang(ms.getLang())
				.resultSets(PagingHelper.join(ms.getResultSets()))
				.build();
	}
}
