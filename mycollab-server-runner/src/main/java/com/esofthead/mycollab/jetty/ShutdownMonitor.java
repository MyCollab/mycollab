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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.thread.ShutdownThread;

/**
 * Shutdown/Stop Monitor thread.
 * This thread listens on the port specified by the STOP.PORT system parameter
 * (defaults to -1 for not listening) for request authenticated with the key
 * given by the STOP.KEY system parameter (defaults to "eclipse") for admin
 * requests.
 * If the stop port is set to zero, then a random port is assigned and the port
 * number is printed to stdout.
 * Commands "stop" and "status" are currently supported.
 */
public class ShutdownMonitor {
	// Implementation of safe lazy init, using Initialization on Demand Holder
	// technique.
	static class Holder {
		static ShutdownMonitor instance = new ShutdownMonitor();
	}

	public static ShutdownMonitor getInstance() {
		return Holder.instance;
	}

	/**
	 * ShutdownMonitorThread
	 * 
	 * Thread for listening to STOP.PORT for command to stop Jetty. If
	 * ShowndownMonitor.exitVm is true, then Sytem.exit will also be called
	 * after the stop.
	 * 
	 */
	public class ShutdownMonitorThread extends Thread {

		public ShutdownMonitorThread() {
			setDaemon(true);
			setName("ShutdownMonitor");
		}

		@Override
		public void run() {
			if (serverSocket == null) {
				return;
			}

			while (serverSocket != null) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();

					LineNumberReader lin = new LineNumberReader(
							new InputStreamReader(socket.getInputStream()));
					String receivedKey = lin.readLine();
					if (!key.equals(receivedKey)) {
						System.err
								.println("Ignoring command with incorrect key");
						continue;
					}

					OutputStream out = socket.getOutputStream();

					String cmd = lin.readLine();
					debug("command=%s", cmd);
					if ("stop".equals(cmd)) {
						// Graceful Shutdown
						debug("Issuing graceful shutdown..");
						ShutdownThread.getInstance().run();

						// Reply to client
						debug("Informing client that we are stopped.");
						out.write("Stopped\r\n".getBytes(StringUtil.__UTF8));
						out.flush();

						// Shutdown Monitor
						debug("Shutting down monitor");
						close(socket);
						socket = null;
						close(serverSocket);
						serverSocket = null;

						if (exitVm) {
							// Kill JVM
							debug("Killing JVM");
							System.exit(0);
						}
					} else if ("status".equals(cmd)) {
						// Reply to client
						out.write("OK\r\n".getBytes(StringUtil.__UTF8));
						out.flush();
					}
				} catch (Exception e) {
					debug(e);
					System.err.println(e.toString());
				} finally {
					close(socket);
					socket = null;
				}
			}
		}

		public void start() {
			if (isAlive()) {
				System.err.printf("ShutdownMonitorThread already started");
				return; // cannot start it again
			}

			startListenSocket();

			if (serverSocket == null) {
				return;
			}
			if (DEBUG)
				System.err.println("Starting ShutdownMonitorThread");
			super.start();
		}

		private void startListenSocket() {
			if (port < 0) {
				if (DEBUG)
					System.err
							.println("ShutdownMonitor not in use (port < 0): "
									+ port);
				return;
			}

			try {
				serverSocket = new ServerSocket(port, 1,
						InetAddress.getByName("127.0.0.1"));
				if (port == 0) {
					// server assigned port in use
					port = serverSocket.getLocalPort();
					System.out.printf("STOP.PORT=%d%n", port);
				}

				if (key == null) {
					// create random key
					key = Long
							.toString((long) (Long.MAX_VALUE * Math.random()
									+ this.hashCode() + System
									.currentTimeMillis()), 36);
					System.out.printf("STOP.KEY=%s%n", key);
				}
			} catch (Exception e) {
				debug(e);
				System.err.println("Error binding monitor port " + port + ": "
						+ e.toString());
				serverSocket = null;
			} finally {
				// establish the port and key that are in use
				debug("STOP.PORT=%d", port);
				debug("STOP.KEY=%s", key);
				debug("%s", serverSocket);
			}
		}

	}

	private boolean DEBUG;
	private int port;
	private String key;
	private boolean exitVm;
	private ServerSocket serverSocket;
	private ShutdownMonitorThread thread;

	/**
	 * Create a ShutdownMonitor using configuration from the System properties.
	 * <p>
	 * <code>STOP.PORT</code> = the port to listen on (empty, null, or values
	 * less than 0 disable the stop ability)<br>
	 * <code>STOP.KEY</code> = the magic key/passphrase to allow the stop
	 * (defaults to "eclipse")<br>
	 * <p>
	 * Note: server socket will only listen on localhost, and a successful stop
	 * will issue a System.exit() call.
	 */
	private ShutdownMonitor() {
		Properties props = System.getProperties();

		this.DEBUG = props.containsKey("DEBUG");

		// Use values passed thru via /jetty-start/
		this.port = Integer.parseInt(props.getProperty("STOP.PORT", "-1"));
		this.key = props.getProperty("STOP.KEY", null);
		this.exitVm = true;
	}

	private void close(ServerSocket server) {
		if (server == null) {
			return;
		}

		try {
			server.close();
		} catch (IOException ignore) {
			/* ignore */
		}
	}

	private void close(Socket socket) {
		if (socket == null) {
			return;
		}

		try {
			socket.close();
		} catch (IOException ignore) {
			/* ignore */
		}
	}

	private void debug(String format, Object... args) {
		if (DEBUG) {
			System.err.printf("[ShutdownMonitor] " + format + "%n", args);
		}
	}

	private void debug(Throwable t) {
		if (DEBUG) {
			t.printStackTrace(System.err);
		}
	}

	public String getKey() {
		return key;
	}

	public int getPort() {
		return port;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public boolean isExitVm() {
		return exitVm;
	}

	public void setDebug(boolean flag) {
		this.DEBUG = flag;
	}

	public void setExitVm(boolean exitVm) {
		synchronized (this) {
			if (thread != null && thread.isAlive()) {
				throw new IllegalStateException(
						"ShutdownMonitorThread already started");
			}
			this.exitVm = exitVm;
		}
	}

	public void setKey(String key) {
		synchronized (this) {
			if (thread != null && thread.isAlive()) {
				throw new IllegalStateException(
						"ShutdownMonitorThread already started");
			}
			this.key = key;
		}
	}

	public void setPort(int port) {
		synchronized (this) {
			if (thread != null && thread.isAlive()) {
				throw new IllegalStateException(
						"ShutdownMonitorThread already started");
			}
			this.port = port;
		}
	}

	protected void start() throws Exception {
		ShutdownMonitorThread t = null;
		synchronized (this) {
			if (thread != null && thread.isAlive()) {
				System.err.printf("ShutdownMonitorThread already started");
				return; // cannot start it again
			}

			thread = new ShutdownMonitorThread();
			t = thread;
		}

		if (t != null)
			t.start();
	}

	protected boolean isAlive() {
		boolean result = false;
		synchronized (this) {
			result = (thread != null && thread.isAlive());
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s[port=%d]", this.getClass().getName(), port);
	}
}
