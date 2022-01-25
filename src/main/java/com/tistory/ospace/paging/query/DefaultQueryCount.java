package com.tistory.ospace.paging.query;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.ospace.paging.PagingHelper;

public class DefaultQueryCount extends QueryTemplate {
	private static final Logger logger = LoggerFactory.getLogger(DefaultQueryCount.class);
	
	private final static Pattern RE_SELECT = Pattern.compile("\\b(SELECT|FROM)\\b");
	
	@Override
	protected MappedStatement onMappedStateMent(MappedStatement ms) {
		return PagingHelper.newMappedStatement(ms, ms.getId()+"Count");
	}

	@Override
	protected BoundSql onBoundSql(MappedStatement ms, BoundSql bs, Map<String, Object> param) {
		String sql = bs.getSql().toUpperCase();
		Matcher matcher = RE_SELECT.matcher(sql);
		int cnt = 0;
		int begin = 0, end = 0;
		while (matcher.find()) {
			if (0 == cnt) {
				begin = matcher.end();
			}
			switch(matcher.group(1)) {
			case "SELECT": ++cnt; break;
			case "FROM": --cnt; break;
			}
			if (0 == cnt) {
				end = matcher.start();
				break;
			}
		}
		
		logger.debug("onBoundSql: begin[{}] end[{}] sql[{}]", begin, end, sql);
		
		StringBuilder sb = new StringBuilder();
		sb.append(sql.substring(0, begin))
			.append(" COUNT(0) ")
			.append(sql.substring(end, sql.length()));
		
		return new BoundSql(ms.getConfiguration(), sb.toString(), bs.getParameterMappings(), param);
	}

}
