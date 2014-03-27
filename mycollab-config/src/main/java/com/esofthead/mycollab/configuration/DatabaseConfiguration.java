/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;

/**
 * Bean contains database configuration
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class DatabaseConfiguration {

	private String driverClass;

	private String dbUrl;

	private String user;

	private String password;

	DatabaseConfiguration(String driverClass, String dbUrl, String user,
			String password) {
		this.user = user;
		this.driverClass = driverClass;
		this.dbUrl = dbUrl;
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
