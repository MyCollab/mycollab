/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;

import com.esofthead.mycollab.core.DeploymentMode;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public abstract class MyCollabAssets {
	private static MyCollabAssets impl;

	static {
		DeploymentMode deploymentMode = SiteConfiguration.getDeploymentMode();
		if (deploymentMode == DeploymentMode.site) {
			impl = new S3();
		} else {
			impl = new Local();
		}
	}

	protected abstract String generateResourceLink(String resourceId);

	public static String newResourceLink(String resourceId) {
		return impl.generateResourceLink(resourceId);
	}

	public static class S3 extends MyCollabAssets {
		private static String S3_ASSETS = "https://s3.amazonaws.com/mycollab_assets/%s";

		@Override
		protected String generateResourceLink(String resourceId) {
			return String.format(S3_ASSETS, resourceId);
		}

	}

	public static class Local extends MyCollabAssets {

		@Override
		protected String generateResourceLink(String resourceId) {
			return String.format(ApplicationProperties
					.getString(ApplicationProperties.APP_URL),
					SiteConfiguration.getServerAddress(), SiteConfiguration
							.getServerPort()) + "assets/" + resourceId;
		}

	}
}
