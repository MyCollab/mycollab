package com.esofthead.mycollab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DatabaseValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String databaseName = request.getParameter("databaseName");
		String dbUserName = request.getParameter("dbUserName");
		String dbPassword = request.getParameter("dbPassword");
		String databaseServer = request.getParameter("databaseServer");

		String dbUrl = String.format("jdbc:mysql://%s/%s?useUnicode=true",
				databaseServer, databaseName);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(dbUrl,
					dbUserName, dbPassword);
			DatabaseMetaData metaData = connection.getMetaData();
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.write("Cannot establish connection to database. Recheck your input.");
		}
	}
}
