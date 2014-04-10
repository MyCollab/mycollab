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
package com.esofthead.mycollab.jetty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.configuration.DatabaseConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.jetty.console.TextDevice;
import com.esofthead.template.velocity.TemplateContext;
import com.esofthead.template.velocity.TemplateEngine;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * Generic MyCollab embedded server
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class GenericServerRunner {
	private static Logger log = LoggerFactory
			.getLogger(GenericServerRunner.class);

	private Server server;
	private int port = 0;
	public static boolean isFirstTimeRunner = false;

	public abstract WebAppContext buildContext(String baseDir);

	/**
	 * Start server
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		server.start();
		server.join();
	}

	/**
	 * Stop server
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		server.stop();
	}

	/**
	 * Detect web app folder
	 * 
	 * @return
	 */
	private String detectWebApp() {
		File webappFolder = new File(System.getProperty("user.dir"), "webapp");

		if (!webappFolder.exists()) {
			webappFolder = new File(System.getProperty("user.dir"),
					"src/main/webapp");
		}

		if (!webappFolder.exists()) {
			throw new MyCollabException("Can not detect webapp base folder");
		} else {
			return webappFolder.getAbsolutePath();
		}
	}

	/**
	 * Run web server with arguments
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void run(String[] args) throws Exception {
		int stopPort = 0;
		String stopKey = null;
		boolean isStop = false;

		for (int i = 0; i < args.length; i++) {
			if ("--stop-port".equals(args[i])) {
				stopPort = Integer.parseInt(args[++i]);
			} else if ("--stop-key".equals(args[i])) {
				stopKey = args[++i];
			} else if ("--stop".equals(args[i])) {
				isStop = true;
			} else if ("--port".equals(args[i])) {
				port = Integer.parseInt(args[++i]);
			}
		}

		switch ((stopPort > 0 ? 1 : 0) + (stopKey != null ? 2 : 0)) {
		case 1:
			usage("Must specify --stop-key when --stop-port is specified");
			break;

		case 2:
			usage("Must specify --stop-port when --stop-key is specified");
			break;

		case 3:
			if (isStop) {
				Socket s = new Socket(InetAddress.getByName("localhost"),
						stopPort);
				try {
					OutputStream out = s.getOutputStream();
					out.write((stopKey + "\r\nstop\r\n").getBytes());
					out.flush();
				} finally {
					s.close();
				}
				return;
			} else {
				ShutdownMonitor monitor = ShutdownMonitor.getInstance();
				monitor.setPort(stopPort);
				monitor.setKey(stopKey);
				monitor.setExitVm(true);

				break;
			}

		}

		execute();

	}

	/**
	 * Detect localtion of config file
	 * 
	 * @param filename
	 * @return
	 */
	private File detectConfigFile(String filename) {
		File confFile = new File(System.getProperty("user.dir"), "conf/"
				+ filename);

		if (!confFile.exists()) {
			confFile = new File(System.getProperty("user.dir"),
					"src/main/conf/" + filename);
		}

		if (!confFile.exists()) {
			return null;
		} else {
			return confFile;
		}
	}

	/**
	 * Pre-Start server process
	 */
	protected void preStartServer() {
		File file = detectConfigFile("mycollab.properties");
		if (file == null) {
			log.debug("Can not detect mycollab.properties file. It seems mycollab is in first use.");
			isFirstTimeRunner = true;
			TemplateContext templateContext = new TemplateContext();

			System.out
					.println("=====================================================");
			System.out
					.println("                    MYCOLLAB SETUP                   ");
			System.out
					.println("=====================================================");

			TextDevice device = TextDevice.defaultTextDevice();
			System.out.println("Enter site name:");
			String sitename = device.readLine();
			templateContext.put("sitename", sitename);

			System.out.println("Enter server address:");
			String serverAddress = device.readLine();
			templateContext.put("serveraddress", serverAddress);

			System.out
					.println("Enter server port (then you can access server with address)");

			int serverPort = 0;

			int numTries = 0;
			while (numTries < 3) {
				String serverPortVal = device.readLine();

				try {
					serverPort = Integer.parseInt(serverPortVal);
					break;
				} catch (Exception e) {
					System.out.println("Port must be the number from 1-65000");
					numTries++;
					if (numTries == 3) {
						System.exit(-1);
					}
				}
			}

			templateContext.put("serverport", serverPort + "");

			System.out
					.println("=====================================================");
			System.out
					.println("                  DATABASE SETUP                     ");
			System.out
					.println("=====================================================");

			numTries = 0;
			while (numTries < 3) {
				templateContext.put("db.driverClassName",
						"com.mysql.jdbc.Driver");

				System.out.println("Enter database server address:");
				String dbServerAddress = device.readLine();

				System.out
						.println("Enter database name (Database must be created before):");
				String dbName = device.readLine();

				String dbUrl = String.format(
						"jdbc:mysql://%s/%s?useUnicode=true", dbServerAddress,
						dbName);
				templateContext.put("dbUrl", dbUrl);

				System.out.println("Enter database user name:");
				String dbUserName = device.readLine();
				templateContext.put("dbUser", dbUserName);

				System.out.println("Enter database user password:");
				String dbPassword = new String(device.readPassword());
				templateContext.put("dbPassword", dbPassword);

				log.debug("Checking database connection ...");
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection connection = DriverManager.getConnection(dbUrl,
							dbUserName, dbPassword);
					DatabaseMetaData metaData = connection.getMetaData();
					break;
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Can not set up database.");
					numTries++;
					if (numTries == 3) {
						System.exit(-1);
					}
				}
			}

			System.out
					.println("=====================================================");
			System.out
					.println("                 EMAIL SETUP  (Optional)             ");
			System.out
					.println("=====================================================");

			System.out
					.println("We need your smtp email configuration to send system notifications such as bug assignment, new account created to your team. If you do not have any smtp account, you can ignore this setting section. Do you want continue to set up stmp settings (y/n):");

			numTries = 0;
			while (numTries < 3) {
				String acceptContinue = device.readLine();
				if ("y".equals(acceptContinue)) {
					System.out.println("Outgoing server address:");
					String stmpHost = device.readLine();
					templateContext.put("smtpAddress", stmpHost);

					System.out.println("Mail server port:");
					int mailServerPort = 0;

					while (true) {
						String serverPortVal = device.readLine();

						try {
							mailServerPort = Integer.parseInt(serverPortVal);
							break;
						} catch (Exception e) {
							System.out
									.println("Port must be the number from 1-65000.");
						}
					}
					templateContext.put("smtpPort", mailServerPort + "");

					System.out.println("Mail user name:");
					String mailUser = device.readLine();
					templateContext.put("smtpUserName", mailUser);

					System.out.println("Mail password:");
					String mailPassword = device.readLine();
					templateContext.put("smtpPassword", mailPassword);

					System.out.println("Enable TLS (y/n):");
					String tlsEnable = device.readLine();
					if (tlsEnable.equals("y")) {
						templateContext.put("smtpTLSEnable", "true");
					} else {
						templateContext.put("smtpTLSEnable", "false");
					}
					break;
				} else if ("n".equals(acceptContinue)) {
					System.out
							.println("You can set up stmp account later in file %MYCOLLAB_HOME%/conf/mycollab.properties");
					templateContext.put("smtpAddress", "");
					templateContext.put("smtpPort", "1");
					templateContext.put("smtpUserName", "");
					templateContext.put("smtpPassword", "");
					templateContext.put("smtpTLSEnable", "false");
					break;
				} else {
					System.out.println("You must select y (yes) or n (no)");
					numTries++;

					if (numTries == 3) {
						System.out
								.println("You can set up stmp account later in file %MYCOLLAB_HOME%/conf/mycollab.properties");
						templateContext.put("smtpAddress", "");
						templateContext.put("smtpPort", "1");
						templateContext.put("smtpUserName", "");
						templateContext.put("smtpPassword", "");
						templateContext.put("smtpTLSEnable", "false");
					}
				}
			}

			templateContext.put("error.sendTo", "hainguyen@esofthead.com");
			templateContext.put("cdn.url", "http://%s:%d/assets/images/email/");
			templateContext.put("app.url", "http://%s:%d/");

			log.debug("Write to properties file");

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

					TemplateEngine.evaluate(templateContext, writer,
							"log task", templateReader);

					FileOutputStream outStream = new FileOutputStream(new File(
							confFolder, "mycollab.properties"));
					outStream.write(writer.toString().getBytes());
					outStream.flush();
					outStream.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.err
							.println("Can not write setting to config file. You should call mycollab support support@mycollab.com to solve this issue.");
					System.exit(1);
				}
			}

		}
	}

	public void execute() throws Exception {
		preStartServer();
		server = new Server((port > 0) ? port
				: SiteConfiguration.getServerPort());
		log.debug("Detect root folder webapp");
		String webappDirLocation = detectWebApp();

		WebAppContext appContext = buildContext(webappDirLocation);
		appContext.setServer(server);
		appContext.setConfigurations(new Configuration[] {
				new AnnotationConfiguration(), new WebXmlConfiguration(),
				new WebInfConfiguration(), new PlusConfiguration(),
				new MetaInfConfiguration(), new FragmentConfiguration(),
				new EnvConfiguration() });

		// Register a mock DataSource scoped to the webapp
		// This must be linked to the webapp via an entry in web.xml:
		// <resource-ref>
		// <res-ref-name>jdbc/mydatasource</res-ref-name>
		// <res-type>javax.sql.DataSource</res-type>
		// <res-auth>Container</res-auth>
		// </resource-ref>
		// At runtime the webapp accesses this as
		// java:comp/env/jdbc/mydatasource
		org.eclipse.jetty.plus.jndi.Resource mydatasource = new org.eclipse.jetty.plus.jndi.Resource(
				appContext, "jdbc/mycollabdatasource", buildDataSource());

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { appContext });
		server.setHandler(handlers);

		server.setStopAtShutdown(true);

		ContextHandlerCollection contextCollection = new ContextHandlerCollection();
		contextCollection.setServer(server);
		contextCollection.setHandlers(new Handler[] { appContext });

		server.start();

		ShutdownMonitor.getInstance().start();

		server.join();
	}

	public void usage(String error) {
		if (error != null)
			System.err.println("ERROR: " + error);
		System.err
				.println("Usage: java -jar runner.jar [--help|--version] [ server opts]");
		System.err.println("Server Options:");
		System.err
				.println(" --version                          - display version and exit");
		System.err
				.println(" --stop-port n                      - port to listen for stop command");
		System.err
				.println(" --stop-key n                       - security string for stop command (required if --stop-port is present)");
		System.exit(1);
	}

	private DataSource buildDataSource() {
		DatabaseConfiguration dbConf = SiteConfiguration
				.getDatabaseConfiguration();
		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setDriverClass(dbConf.getDriverClass());
		dataSource.setJdbcUrl(dbConf.getDbUrl());
		dataSource.setUsername(dbConf.getUser());
		dataSource.setPassword(dbConf.getPassword());
		dataSource.setIdleConnectionTestPeriodInMinutes(1);
		dataSource.setIdleMaxAgeInMinutes(4);
		dataSource.setMaxConnectionsPerPartition(5);
		dataSource.setMinConnectionsPerPartition(1);
		dataSource.setPoolAvailabilityThreshold(5);
		dataSource.setPartitionCount(1);
		dataSource.setAcquireIncrement(3);
		dataSource.setConnectionTestStatement("SELECT 1");
		return dataSource;
	}
}
