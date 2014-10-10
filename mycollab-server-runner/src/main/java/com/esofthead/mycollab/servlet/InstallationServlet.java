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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class InstallationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(InstallationServlet.class);

	private boolean waitFlag = true;

	public void setWaitFlag(boolean flag) {
		this.waitFlag = flag;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sitename = request.getParameter("sitename");
		String serverAddress = request.getParameter("serverAddress");
		String databaseName = request.getParameter("databaseName");
		String dbUserName = request.getParameter("dbUserName");
		String dbPassword = request.getParameter("dbPassword");
		String databaseServer = request.getParameter("databaseServer");
		String smtpUserName = request.getParameter("smtpUserName");
		String smtpPassword = request.getParameter("smtpPassword");
		String smtpHost = request.getParameter("smtpHost");
		String smtpPort = request.getParameter("smtpPort");
		String tls = request.getParameter("tls");

		VelocityContext templateContext = new VelocityContext();
		templateContext.put("sitename", sitename);
		templateContext.put("serveraddress", serverAddress);

		String dbUrl = String
				.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8&autoReconnect=true",
						databaseServer, databaseName);
		templateContext.put("dbUrl", dbUrl);
		templateContext.put("dbUser", dbUserName);
		templateContext.put("dbPassword", dbPassword);
		templateContext.put("smtpAddress", smtpHost);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(dbUrl,
					dbUserName, dbPassword);
			connection.getMetaData();
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.write("Cannot establish connection to database. Make sure your inputs are correct.");
			return;
		}

		int mailServerPort = 1;
		try {
			mailServerPort = Integer.parseInt(smtpPort);
		} catch (Exception e) {

		}

		templateContext.put("smtpPort", mailServerPort + "");
		templateContext.put("smtpUserName", smtpUserName);
		templateContext.put("smtpPassword", smtpPassword);
		templateContext.put("smtpTLSEnable", tls);

		File confFolder = new File(System.getProperty("user.dir"), "conf");

		if (!confFolder.exists()) {
			confFolder = new File(System.getProperty("user.dir"),
					"src/main/conf");
		}

		if (!confFolder.exists()) {
			throw new MyCollabException("Can not detect webapp base folder");
		} else {
			try {
				File templateFile = new File(confFolder,
						"mycollab.properties.template");
				FileReader templateReader = new FileReader(templateFile);

				StringWriter writer = new StringWriter();

				VelocityEngine engine = new VelocityEngine();
				engine.evaluate(templateContext, writer, "log task",
						templateReader);

				FileOutputStream outStream = new FileOutputStream(new File(
						confFolder, "mycollab.properties"));
				outStream.write(writer.toString().getBytes());
				outStream.flush();
				outStream.close();

				while (waitFlag == true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						throw new MyCollabException(e);
					}
				}

			} catch (Exception e) {
				log.error("Error while set up MyCollab", e);
				PrintWriter out = response.getWriter();
				out.write("Can not write setting to config file. You should contact mycollab support support@mycollab.com to solve this issue.");
				return;
			}
		}

	}
}
