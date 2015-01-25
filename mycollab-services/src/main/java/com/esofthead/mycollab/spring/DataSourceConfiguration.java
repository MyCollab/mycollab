/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.spring;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.6.0
 *
 */
@Configuration
@Profile("production")
@MapperScan("com.esofthead.mycollab.**.dao")
@EnableTransactionManagement
public class DataSourceConfiguration {

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		JndiDataSourceLookup ds = new JndiDataSourceLookup();
		ds.setResourceRef(true);
		DataSource dataSource = ds
				.getDataSource("java:comp/env/jdbc/mycollabdatasource");
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager txManager() {
		DataSourceTransactionManager bean = new DataSourceTransactionManager(dataSource());
		return bean;
	}
}
