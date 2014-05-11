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
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.LifeCycle;
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
import com.esofthead.mycollab.servlet.AssetHttpServletRequestHandler;
import com.esofthead.mycollab.servlet.DatabaseValidate;
import com.esofthead.mycollab.servlet.EmailValidationServlet;
import com.esofthead.mycollab.servlet.InstallationServlet;
import com.esofthead.mycollab.servlet.SetupServlet;
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

	private InstallationServlet install;

	private ServletContextHandler installationContextHandler;
	private ContextHandlerCollection contexts;

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

	public void execute() throws Exception {
		server = new Server((port > 0) ? port : 8080);

		contexts = new ContextHandlerCollection();

		if (!checkConfigFileExist()) {
			installationContextHandler = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			installationContextHandler.setContextPath("/");

			install = new InstallationServlet();
			installationContextHandler.addServlet(new ServletHolder(install),
					"/install");
			installationContextHandler.addServlet(new ServletHolder(
					new DatabaseValidate()), "/validate");
			installationContextHandler.addServlet(new ServletHolder(
					new EmailValidationServlet()), "/emailValidate");

			installationContextHandler.addServlet(new ServletHolder(
					new AssetHttpServletRequestHandler()), "/assets/*");
			installationContextHandler.addServlet(new ServletHolder(
					new SetupServlet()), "/*");
			installationContextHandler
					.addLifeCycleListener(new ServerLifeCycleListener(server));

			server.setStopAtShutdown(true);
			contexts.setHandlers(new Handler[] { installationContextHandler });
		} else {
			WebAppContext appContext = initWebAppContext();
			contexts.addHandler(appContext);
		}

		server.setHandler(contexts);
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
		SiteConfiguration.loadInstance();
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

	private boolean checkConfigFileExist() {
		File confFolder = new File(System.getProperty("user.dir"), "conf");

		if (!confFolder.exists()) {
			confFolder = new File(System.getProperty("user.dir"),
					"src/main/conf");
		}

		if (!confFolder.exists()) {
			throw new MyCollabException("Can not detect webapp base folder");
		} else {
			File confFile = new File(confFolder, "mycollab.properties");
			return confFile.exists();
		}
	}

	private WebAppContext initWebAppContext() {
		String webappDirLocation = detectWebApp();
		WebAppContext appContext = buildContext(webappDirLocation);
		appContext.setServer(server);
		appContext.setConfigurations(new Configuration[] {
				new AnnotationConfiguration(), new WebXmlConfiguration(),
				new WebInfConfiguration(), new PlusConfiguration(),
				new MetaInfConfiguration(), new FragmentConfiguration(),
				new EnvConfiguration() });

		// Register a mock DataSource scoped to the webapp
		// This must be linked to the webapp via an entry in
		// web.xml:
		// <resource-ref>
		// <res-ref-name>jdbc/mydatasource</res-ref-name>
		// <res-type>javax.sql.DataSource</res-type>
		// <res-auth>Container</res-auth>
		// </resource-ref>
		// At runtime the webapp accesses this as
		// java:comp/env/jdbc/mydatasource
		try {
			org.eclipse.jetty.plus.jndi.Resource mydatasource = new org.eclipse.jetty.plus.jndi.Resource(
					appContext, "jdbc/mycollabdatasource", buildDataSource());
		} catch (NamingException e) {
			throw new MyCollabException(e);
		}

		return appContext;
	}

	private class ServerLifeCycleListener implements LifeCycle.Listener {

		private Server server;

		public ServerLifeCycleListener(Server server) {
			this.server = server;
		}

		@Override
		public void lifeCycleStarting(LifeCycle event) {

		}

		@Override
		public void lifeCycleStarted(LifeCycle event) {
			System.out.println("Started");

			Runnable thread = new Runnable() {

				@Override
				public void run() {
					log.debug("Detect root folder webapp");
					File confFolder = new File(System.getProperty("user.dir"),
							"conf");

					if (!confFolder.exists()) {
						confFolder = new File(System.getProperty("user.dir"),
								"src/main/conf");
					}

					if (!confFolder.exists()) {
						throw new MyCollabException(
								"Can not detect webapp base folder");
					} else {
						File confFile = new File(confFolder,
								"mycollab.properties");
						while (!confFile.exists()) {
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								throw new MyCollabException(e);
							}
						}

						WebAppContext appContext = initWebAppContext();
						contexts.removeHandler(installationContextHandler);
						contexts.addHandler(appContext);
						install.setWaitFlag(false);
					}
				}
			};

			new Thread(thread).start();
		}

		@Override
		public void lifeCycleFailure(LifeCycle event, Throwable cause) {

		}

		@Override
		public void lifeCycleStopping(LifeCycle event) {

		}

		@Override
		public void lifeCycleStopped(LifeCycle event) {

		}

	}
}
