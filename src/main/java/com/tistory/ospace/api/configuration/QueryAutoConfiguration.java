package com.tistory.ospace.api.configuration;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.tistory.ospace.paging.configuration.QueryInterceptor;

@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class QueryAutoConfiguration implements InitializingBean {
	//private static final Logger logger = LoggerFactory.getLogger(QueryAutoConfiguration.class);
	private static final QueryInterceptor interceptor = new QueryInterceptor();;

	@Autowired
	private List<SqlSessionFactory> sqlSessionFactoryList;

	@Override
	public void afterPropertiesSet() throws Exception {
		//logger.info("afterPropertiesSet begin:");

		for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
			org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
			if (configuration.getInterceptors().contains(interceptor)) continue;
			configuration.addInterceptor(interceptor);
		}		
	}
}