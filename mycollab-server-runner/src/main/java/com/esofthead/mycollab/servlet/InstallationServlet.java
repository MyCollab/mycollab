package com.esofthead.mycollab.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
import com.esofthead.mycollab.jetty.GenericServerRunner;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class InstallationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory
			.getLogger(GenericServerRunner.class);


	
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
		String stmpHost = request.getParameter("stmpHost");
		String smtpPort = request.getParameter("smtpPort");
		String tls = request.getParameter("tls");

		VelocityContext templateContext = new VelocityContext();
		templateContext.put("sitename", sitename);
		templateContext.put("serveraddress", serverAddress);
		templateContext.put("serverport", "8080");

		String dbUrl = String.format("jdbc:mysql://%s/%s?useUnicode=true",
				databaseServer, databaseName);
		templateContext.put("dbUrl", dbUrl);
		templateContext.put("dbUser", dbUserName);
		templateContext.put("dbPassword", dbPassword);
		templateContext.put("smtpAddress", stmpHost);
		int mailServerPort = 1;
		try {
			mailServerPort = Integer.parseInt(smtpPort);
		} catch (Exception e) {

		}
		templateContext.put("smtpPort", mailServerPort + "");
		templateContext.put("smtpUserName", smtpUserName);
		templateContext.put("smtpPassword", smtpPassword);
		templateContext.put("smtpTLSEnable", tls);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(dbUrl,
					dbUserName, dbPassword);
			DatabaseMetaData metaData = connection.getMetaData();
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.write("Cannot establish connection to database. Make sure your inputs are correct.");
			return;
		}

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
			} catch (Exception e) {
				e.printStackTrace();
				PrintWriter out = response.getWriter();
				out.write("Can not write setting to config file. You should contact mycollab support support@mycollab.com to solve this issue.");
				return;
			}
		}

	}
}
