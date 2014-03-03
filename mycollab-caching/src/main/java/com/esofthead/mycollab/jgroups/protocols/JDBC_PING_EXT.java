/**
 * This file is part of mycollab-caching.
 *
 * mycollab-caching is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-caching is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-caching.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.jgroups.protocols;

import org.jgroups.conf.ClassConfigurator;
import org.jgroups.protocols.JDBC_PING;

public class JDBC_PING_EXT extends JDBC_PING {

	static {
		ClassConfigurator.add((short) 2048, JDBC_PING_EXT.class);
	}

	@Override
	public void init() throws Exception {
		this.datasource_jndi_name = "java:comp/env/jdbc/mycollabdatasource";
		this.initialize_sql = " CREATE TABLE IF NOT EXISTS JGROUPSPING (own_addr varchar(200) NOT NULL, "
				+ "cluster_name varchar(200) NOT NULL,"
				+ "ping_data varbinary(5000) DEFAULT NULL, "
				+ "PRIMARY KEY (own_addr, cluster_name) ) "
				+ "ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		this.id = 2048;
		super.init();
	}
}
