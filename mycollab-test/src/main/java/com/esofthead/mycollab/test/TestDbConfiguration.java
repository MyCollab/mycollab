package com.esofthead.mycollab.test;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TestDbConfiguration {
	private Properties props;

	public TestDbConfiguration() {
		InputStream stream = DataSourceFactoryBean.class.getClassLoader()
				.getResourceAsStream("mycollab-test.properties");
		if (stream == null) {
			stream = DataSourceFactoryBean.class.getClassLoader()
					.getResourceAsStream("default-mycollab-test.properties");
		}

		props = new Properties();
		if (stream != null) {
			try {
				props.load(stream);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String getDriverClassName() {
		return props.getProperty("db.driverClassName", "com.mysql.jdbc.Driver");
	}

	public String getJdbcUrl() {
		return props.getProperty("db.url",
				"jdbc:mysql://localhost/mycollab_test?autoReconnect=true");
	}

	public String getUsername() {
		return props.getProperty("db.username", "root");
	}

	public String getPassword() {
		return props.getProperty("db.password", "esofthead321");
	}
}
