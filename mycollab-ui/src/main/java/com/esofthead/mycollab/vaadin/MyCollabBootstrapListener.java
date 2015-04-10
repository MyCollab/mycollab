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
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class MyCollabBootstrapListener implements BootstrapListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
	}

	@Override
	public void modifyBootstrapPage(BootstrapPageResponse response) {
//		response.getDocument().head().append(
//				"<meta http-equiv=\"pragma\" content=\"no-cache\">");
//		response.getDocument().head().append(
//				"<meta http-equiv=\"cache-control\" content=\"no-cache\">");
//		response.getDocument().head().append(
//				"<meta http-equiv=\"cache-control\" content=\"max-age=0\">");
//		response.getDocument().head().append(
//				"<meta http-equiv=\"expires\" content=\"-1\">");


		response.getDocument().head()
				.append("<meta name=\"robots\" content=\"nofollow\" />");

		DeploymentMode deploymentMode = SiteConfiguration.getDeploymentMode();
		if (deploymentMode == DeploymentMode.site) {
			response.getDocument()
					.head()
					.append("<script type=\"text/javascript\" src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>");

			response.getDocument()
					.head()
					.append("<script type=\"text/javascript\" src=\"https://s3.amazonaws.com/mycollab_assets/assets/js/stickytooltip.js\"></script>");
		} else {
			response.getDocument()
					.head()
					.append("<script type=\"text/javascript\" src=\"/assets/js/jquery-1.10.2.min.js\"></script>");

			response.getDocument()
					.head()
					.append("<script type=\"text/javascript\" src=\"/assets/js/stickytooltip.js\"></script>");
		}
	}

}
