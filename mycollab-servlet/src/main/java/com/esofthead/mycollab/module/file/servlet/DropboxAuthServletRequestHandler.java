/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.file.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.infinispan.commons.api.BasicCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.esofthead.mycollab.cache.LocalCacheManager;
import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.ecm.StorageNames;
import com.esofthead.mycollab.module.ecm.esb.CloudDriveOAuthCallbackEvent;
import com.esofthead.mycollab.module.file.CloudDriveInfo;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("dropboxAuthServlet")
public class DropboxAuthServletRequestHandler extends GenericServletRequestHandler {
	private static Logger log = LoggerFactory
			.getLogger(DropboxAuthServletRequestHandler.class);

	private DbxWebAuth getWebAuth(final HttpServletRequest request) {
		java.util.Locale locale = new Locale(Locale.US.getLanguage(),
				Locale.US.getCountry());
		String userLocale = locale.toString();
		DbxRequestConfig requestConfig = new DbxRequestConfig("text-edit/0.1",
				userLocale);
		DbxAppInfo appInfo = new DbxAppInfo("y43ga49m30dfu02",
				"rheskqqb6f8fo6a");
		String redirectUri = SiteConfiguration.getDropboxCallbackUrl();
		HttpSession session = request.getSession(true);
		String sessionKey = "dropbox-auth-csrf-token";
		DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session,
				sessionKey);
		String stateParam = request.getParameter("state");
		if (stateParam == null || stateParam.equals("")) {
			throw new MyCollabException(
					"Can not get state parameter successfully, Invalid request");
		}

		int index = stateParam.indexOf("|");
		if (index < 0) {
			throw new MyCollabException("Invalid parameter request "
					+ stateParam);
		}

		String oldSessionId = stateParam.substring(index + 1);
		BasicCache<String, Object> cache = LocalCacheManager
				.getCache(oldSessionId);
		Object csrfTokenVal = cache.get(sessionKey);

		if (csrfTokenVal == null) {
			throw new MyCollabException(
					"Invalid parameter request, can not define csrfToken");
		} else {
			csrfTokenStore.set((String) csrfTokenVal);
		}

		DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo,
				redirectUri, csrfTokenStore);
		return webAuth;
	}

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DbxAuthFinish authFinish = null;
		try {
			authFinish = getWebAuth(request).finish(request.getParameterMap());
		} catch (Exception e) {
			log.error("Authorize dropbox request failed", e);
			PrintWriter out = response.getWriter();
			out.println("<html>"
					+ "<body></body>"
					+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
					+ "<script>");
			out.println("$(document).ready(function(){" + "window.close();"
					+ "});");
			out.println("</script>");
			out.println("</html>");
			return;
		}

		String accessToken = authFinish.accessToken;
		String appId = authFinish.urlState;
		if (appId.startsWith("|")) {
			appId = appId.substring(1);
		}

		// Store accessToken ...
		CloudDriveInfo cloudDriveInfo = new CloudDriveInfo(
				StorageNames.DROPBOX, accessToken);

		BasicCache<String, Object> cache = LocalCacheManager.getCache(appId);

		EventBus eventBus = (EventBus) cache.get(MyCollabSession.EVENT_BUS_VAL);
		if (eventBus != null) {
			eventBus.fireEvent(new CloudDriveOAuthCallbackEvent.ReceiveCloudDriveInfo(
					DropboxAuthServletRequestHandler.this, cloudDriveInfo));
		} else {
			log.error(
					"Can not find eventbus for session id {}, this session is not initialized by user yet",
					appId);
		}

		// response script close current window
		PrintWriter out = response.getWriter();
		out.println("<html>"
				+ "<body></body>"
				+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
				+ "<script>");
		out.println("$(document).ready(function(){" + "window.close();" + "});");
		out.println("</script>");
		out.println("</html>");
	}

}
