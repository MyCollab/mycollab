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
package com.mycollab.servlet;

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.FileUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class InstallationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(InstallationServlet.class);

    private boolean waitFlag = true;

    public void setWaitFlag(boolean flag) {
        this.waitFlag = flag;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("Try to install MyCollab");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "-1");
        PrintWriter out = response.getWriter();
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
        String ssl = request.getParameter("ssl");

        String dbUrl = String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true&useCompression=true&useServerPrepStmts=false",
                databaseServer, databaseName);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.error("Can not load mysql driver", e);
            return;
        }

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            LOG.debug("Check database config");
            connection.getMetaData();
        } catch (Exception e) {
            String rootCause = (e.getCause() == null) ? e.getMessage() : e.getCause().getMessage();
            out.write("Cannot establish connection to database. Make sure your inputs are correct. Root cause is " + rootCause);
            LOG.error("Can not connect database", e);
            return;
        }

        int mailServerPort;
        try {
            mailServerPort = Integer.parseInt(smtpPort);
        } catch (Exception e) {
            mailServerPort = 0;
        }

        boolean isStartTls = Boolean.parseBoolean(tls);
        boolean isSsl = Boolean.parseBoolean(ssl);
        try {
            InstallUtils.checkSMTPConfig(smtpHost, mailServerPort, smtpUserName, smtpPassword, true, isStartTls, isSsl);
        } catch (UserInvalidInputException e) {
            LOG.warn("Cannot authenticate mail server successfully. Make sure your inputs are correct.");
        }

        Configuration configuration = SiteConfiguration.freemarkerConfiguration();
        Map<String, Object> templateContext = new HashMap<>();
        templateContext.put("sitename", sitename);
        templateContext.put("serveraddress", serverAddress);
        templateContext.put("dbUrl", dbUrl);
        templateContext.put("dbUser", dbUserName);
        templateContext.put("dbPassword", dbPassword);
        templateContext.put("smtpAddress", smtpHost);
        templateContext.put("smtpPort", mailServerPort + "");
        templateContext.put("smtpUserName", smtpUserName);
        templateContext.put("smtpPassword", smtpPassword);
        templateContext.put("smtpTLSEnable", tls);
        templateContext.put("smtpSSLEnable", ssl);
        templateContext.put("mailNotify", smtpUserName);

        File confFolder = FileUtils.getDesireFile(FileUtils.getUserFolder(), "conf", "src/main/conf");
        if (confFolder == null) {
            out.write("Can not write the settings to the file system. You should check our knowledge base article at " +
                    "http://support.mycollab.com/topic/994098-/ to solve this issue.");
            return;
        }

        if (!Files.isWritable(FileSystems.getDefault().getPath(confFolder.getAbsolutePath()))) {
            out.write("The folder " + confFolder.getAbsolutePath() + " has no write permission with the current user." +
                    " You should set the write permission for MyCollab process for this folder.  You should check our knowledge base article at http://support.mycollab.com/topic/994098-/ to solve this issue.");
            return;
        }

        try {
            StringWriter writer = new StringWriter();
            Template template = configuration.getTemplate("mycollab.properties.template");
            template.process(templateContext, writer);

            FileOutputStream outStream = new FileOutputStream(new File(confFolder, "mycollab.properties"));
            outStream.write(writer.toString().getBytes());
            outStream.flush();
            outStream.close();

            while (waitFlag) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new MyCollabException(e);
                }
            }


        } catch (Exception e) {
            LOG.error("Error while set up MyCollab", e);
            out.write("Can not write the settings to the file system. You should check our knowledge base article at " +
                    "http://support.mycollab.com/topic/994098-/ to solve this issue.");
        }
    }
}
