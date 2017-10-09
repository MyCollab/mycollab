package com.mycollab.test.spring;

import com.mycollab.test.TestDbConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DataSourceFactoryBean extends AbstractFactoryBean<DataSource> {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceFactoryBean.class);

    private HikariDataSource dataSource;

    public DataSource getDataSource() {
        try {
            return createInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected DataSource createInstance() throws Exception {
        TestDbConfiguration dbConf = new TestDbConfiguration();
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(20);
        config.setDriverClassName(dbConf.getDriverClassName());
        config.setJdbcUrl(dbConf.getJdbcUrl());
        config.addDataSourceProperty("user", dbConf.getUsername());
        config.addDataSourceProperty("password", dbConf.getPassword());

        LOG.info("Build data source with info " + dbConf.getJdbcUrl() + "&user=" + dbConf.getUsername() +
                "&password=" + dbConf.getPassword());

        dataSource = new HikariDataSource(config);
        Properties dsProperties = new Properties();
        dsProperties.setProperty("cachePrepStmts", "true");
        dsProperties.setProperty("prepStmtCacheSize", "250");
        dsProperties.setProperty("prepStmtCacheSqlLimit", "2048");
        dsProperties.setProperty("useServerPrepStmts", "true");
        dataSource.setDataSourceProperties(dsProperties);
        return dataSource;
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();

        if (dataSource != null) {
            dataSource.close();
            LOG.debug("Close connection");
        }
    }

    @Override
    public Class<DataSource> getObjectType() {
        return DataSource.class;
    }

}
