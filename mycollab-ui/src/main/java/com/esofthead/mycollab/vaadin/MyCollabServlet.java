/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.servlet.MainServlet;
import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.artur.icepush.JavascriptProvider;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.TouchKitSettings;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class MyCollabServlet extends TouchKitServlet {
	private static final long serialVersionUID = 1L;

	private MyCollabUIProvider uiProvider = new MyCollabUIProvider();

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		TouchKitSettings s = getTouchKitSettings();
		s.getApplicationCacheSettings().setCacheManifestEnabled(false);

		getService().addSessionInitListener(new SessionInitListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void sessionInit(SessionInitEvent event) {
				event.getSession().addBootstrapListener(
						new MyCollabBootstrapListener());

				event.getSession().addUIProvider(uiProvider);
			}
		});
	}

	private MainServlet ICEPushServlet;

	private JavascriptProvider javascriptProvider;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		try {
			super.init(servletConfig);
		} catch (ServletException e) {
			if (e.getMessage().equals(
					"Application not specified in servlet parameters")) {
				// Ignore if application is not specified to allow the same
				// servlet to be used for only push in portals
			} else {
				throw e;
			}
		}

		ICEPushServlet = new MainServlet(servletConfig.getServletContext());

		try {
			javascriptProvider = new JavascriptProvider(getServletContext()
					.getContextPath());

			ICEPush.setCodeJavascriptLocation(javascriptProvider
					.getCodeLocation());
		} catch (IOException e) {
			throw new ServletException("Error initializing JavascriptProvider",
					e);
		}
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null
				&& pathInfo.equals("/" + javascriptProvider.getCodeName())) {
			// Serve icepush.js
			serveIcePushCode(request, response);
			return;
		}

		if (request.getRequestURI().endsWith(".icepush")) {
			// Push request
			try {
				ICEPushServlet.service(request, response);
			} catch (ServletException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			// Vaadin request
			super.service(request, response);
		}
	}

	private void serveIcePushCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String icepushJavscript = javascriptProvider.getJavaScript();

		response.setHeader("Content-Type", "text/javascript");
		response.getOutputStream().write(icepushJavscript.getBytes());
	}

	@Override
	public void destroy() {
		super.destroy();
		ICEPushServlet.shutdown();
	}
}
