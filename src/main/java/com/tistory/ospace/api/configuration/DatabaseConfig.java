package com.tistory.ospace.api.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tistory.ospace.paging.configuration.QueryInterceptor;

@Configuration
public class DatabaseConfig {
	@Bean
    SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationCtx) throws Exception {
	    String packagePath = this.getClass().getName();
	    int pos = packagePath.lastIndexOf('.', packagePath.lastIndexOf('.', packagePath.length())-1);
	    packagePath = packagePath.substring(0, pos)+".repository.dto";

        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setMapUnderscoreToCamelCase(true);
        
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(applicationCtx.getResources("classpath*:mapper/**/*.xml"));;
        factoryBean.setTypeAliasesPackage(packagePath);
        factoryBean.setConfiguration(config);
        factoryBean.setVfs(SpringBootVFS.class);
        
        return factoryBean.getObject();
    }
	
	@Bean
	public QueryInterceptor queryInterceptor() {
		return new QueryInterceptor();
	}
}
