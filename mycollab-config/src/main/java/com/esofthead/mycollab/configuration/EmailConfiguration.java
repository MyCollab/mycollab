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
 * Email configuration of MyCollab
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class EmailConfiguration {
	private String host;
	private String user;
	private String password;
	private int port;
	private boolean isTls;

	EmailConfiguration(String host, String username, String password, int port,
			boolean isTLS) {
		this.host = host;
		this.user = username;
		this.password = password;
		this.port = port;
		this.isTls = isTLS;
	}

	public String getHost() {
		return host;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public boolean isTls() {
		return isTls;
	}

}
