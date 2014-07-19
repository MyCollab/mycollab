package com.esofthead.mycollab.vaadin;

import static com.esofthead.mycollab.common.MyCollabSession.CURRENT_APP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.common.SessionIdGenerator;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.arguments.GroupIdProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.2
 *
 */
public abstract class MyCollabUI extends UI {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(MyCollabUI.class);

	static {
		GroupIdProvider.registerAccountIdProvider(new GroupIdProvider() {

			@Override
			public Integer getGroupId() {
				return AppContext.getAccountId();
			}
		});

		SessionIdGenerator.registerSessionIdGenerator(new SessionIdGenerator() {

			@Override
			public String getSessionIdApp() {
				return UI.getCurrent().toString();
			}
		});
	}

	/**
	 * Context of current logged in user
	 */
	protected AppContext currentContext;

	protected String initialSubDomain = "1";
	protected String initialUrl = "";

	public static MyCollabUI getInstance() {
		return (MyCollabUI) MyCollabSession.getVariable(CURRENT_APP);
	}

	public String getInitialUrl() {
		return initialUrl;
	}

	protected void postSetupApp(VaadinRequest request) {
		VaadinServletRequest servletRequest = (VaadinServletRequest) request;
		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.SITE) {
			initialSubDomain = servletRequest.getServerName().split("\\.")[0];
		} else {
			initialSubDomain = servletRequest.getServerName();
		}
	}

	@Override
	public void close() {
		log.debug("Application is closed. Clean all resources");
		currentContext.clearSession();
		currentContext = null;
		super.close();
	}
}
