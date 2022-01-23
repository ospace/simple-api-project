package com.tistory.ospace.paging.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.RowBounds;

import com.tistory.ospace.paging.base.PagingUtils;

public class DefaultQueryList extends QueryTemplate {
	private static final String KEY_LIMIT = "O_LIMIT";
	private static final String KEY_OFFSET = "O_OFFSET";
	
	@Override
	protected MappedStatement onMappedStateMent(MappedStatement ms) {
		return ms;
	}

	@Override
	protected BoundSql onBoundSql(MappedStatement ms, BoundSql bs, Map<String, Object> param) {
		StringBuilder sb = new StringBuilder();
		sb.append(bs.getSql());
		sb.append(" LIMIT ? OFFSET ?");
		
		RowBounds rowBounds = PagingUtils.getRowBounds();
		PagingUtils.resetRowBounds();
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		
		List<ParameterMapping> parameterMappings = new ArrayList<>(bs.getParameterMappings());
		parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), KEY_LIMIT, Integer.class).build());
		parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), KEY_OFFSET, Integer.class).build());
		
		param.put(KEY_LIMIT, limit);
		param.put(KEY_OFFSET, offset);

		return new BoundSql(ms.getConfiguration(), sb.toString(), parameterMappings, param);
	}
}
