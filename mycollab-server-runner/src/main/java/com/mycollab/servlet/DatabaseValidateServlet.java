package com.mycollab.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class DatabaseValidateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseValidateServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String databaseName = request.getParameter("databaseName");
        String dbUserName = request.getParameter("dbUserName");
        String dbPassword = request.getParameter("dbPassword");
        String databaseServer = request.getParameter("databaseServer");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.error("Can not load mysql driver", e);
        }

        String dbUrl = String.format("jdbc:mysql://%s/%s?useUnicode=true", databaseServer, databaseName);
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            connection.getMetaData();
        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            out.write("Cannot establish connection to database. Recheck your input.");
            LOG.warn("Can not connect database", e);
        }
    }
}
