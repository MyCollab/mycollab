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

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.MyCollabException;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class MyCollabUIProvider extends UIProvider {
	private static final long serialVersionUID = 1L;

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		String userAgent = "";
		try {
			userAgent = event.getRequest().getHeader("user-agent")
					.toLowerCase();
		} catch (Exception e) {
			// exception is thrown when request is bot that can not detect
			// user-agent
		}

		String uiClass = "";

		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.SITE) {
			uiClass = "com.esofthead.mycollab.web.DesktopApplication";
		} else {
			if (userAgent.contains("mobile") || userAgent.contains("firefox")) {
				uiClass = "com.esofthead.mycollab.mobile.MobileApplication";
			} else {
				uiClass = "com.esofthead.mycollab.web.DesktopApplication";
			}
		}

		try {
			return (Class<? extends UI>) Class.forName(uiClass);
		} catch (ClassNotFoundException e) {
			throw new MyCollabException(e);
		}
	}

}
