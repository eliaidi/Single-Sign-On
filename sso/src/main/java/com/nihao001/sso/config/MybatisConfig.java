package com.nihao001.sso.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration  
@AutoConfigureAfter({ DataSourceConfig.class }) 
public class MybatisConfig {
	@Resource(name= "dataSource")
    private DataSource dataSource;


    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name="sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
            return getSqlSessionTemplate(dataSource);
    }

    private SqlSessionTemplate getSqlSessionTemplate(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setFailFast(true);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:/com/sumscope/sso/dao/mapping/*.xml"));
        return new SqlSessionTemplate(sessionFactory.getObject());
    }
}
