/**
 * This file is part of mycollab-test.
 *
 * mycollab-test is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-test is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-test.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.test;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class DataSourceFactoryBean extends AbstractFactoryBean<DataSource> {

	private static Logger log = LoggerFactory
			.getLogger(DataSourceFactoryBean.class);

	private BasicDataSource dataSource;

	public DataSource getDataSource() {
		try {
			return (DataSource) createInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected DataSource createInstance() throws Exception {
		InputStream stream = DataSourceFactoryBean.class.getClassLoader()
				.getResourceAsStream("mycollab-test.properties");
		if (stream == null) {
			stream = DataSourceFactoryBean.class.getClassLoader()
					.getResourceAsStream("default-mycollab-test.properties");
		}

		Properties props = new Properties();
		if (stream != null) {
			props.load(stream);
		}

		log.debug("Use database settings is: " + props);

		dataSource = new BasicDataSource();
		dataSource.setDriverClassName(props.getProperty("db.driverClassName",
				"com.mysql.jdbc.Driver"));
		dataSource.setUrl(props.getProperty("db.url",
				"jdbc:mysql://localhost/mycollab_test?autoReconnect=true"));
		dataSource.setUsername(props.getProperty("db.username", "root"));
		dataSource
				.setPassword(props.getProperty("db.password", "esofthead321"));
		return dataSource;
	}

	@Override
	public void destroy() throws Exception {
		super.destroy();

		if (dataSource != null) {
			dataSource.close();
			log.debug("Close connection");
		}

	}

	@Override
	public Class<DataSource> getObjectType() {
		return DataSource.class;
	}

}
