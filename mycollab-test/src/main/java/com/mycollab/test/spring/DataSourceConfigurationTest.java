package com.mycollab.test.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@Configuration
@Profile("test")
public class DataSourceConfigurationTest {

    @Bean
    public DataSource dataSource() {
        return new DataSourceFactoryBean().getDataSource();
    }

    @Bean
    public DataSourceTransactionManager txManager() {
        DataSourceTransactionManager bean = new DataSourceTransactionManager();
        bean.setDataSource(dataSource());
        return bean;
    }
}
