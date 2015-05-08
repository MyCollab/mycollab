/**
 * This file is part of mycollab-server-runner.
 *
 * mycollab-server-runner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-server-runner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-server-runner.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class DatabaseValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseValidate.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String databaseName = request.getParameter("databaseName");
		String dbUserName = request.getParameter("dbUserName");
		String dbPassword = request.getParameter("dbPassword");
		String databaseServer = request.getParameter("databaseServer");

		String dbUrl = String.format("jdbc:mysql://%s/%s?useUnicode=true", databaseServer, databaseName);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			connection.getMetaData();
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.write("Cannot establish connection to database. Recheck your input.");
			LOG.warn("Can not connect database", e);
		}
	}
}
