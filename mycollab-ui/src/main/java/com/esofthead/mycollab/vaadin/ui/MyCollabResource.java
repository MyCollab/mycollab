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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.configuration.S3AssetsResource;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class MyCollabResource {

	private static MyCollabResource impl;

	static {
		DeploymentMode deploymentMode = SiteConfiguration.getDeploymentMode();
		if (deploymentMode == DeploymentMode.SITE) {
			impl = new S3Resource();
		} else {
			impl = new VaadinThemeResource();
		}
	}

	protected abstract Resource generateResource(String resourceId);

	protected abstract String generateResourceLink(String resourceId);

	public static Resource newResource(String resourceId) {
		return impl.generateResource(resourceId);
	}

	public static String newResourceLink(String resourceId) {
		return impl.generateResourceLink(resourceId);
	}

	public static class S3Resource extends MyCollabResource {

		private static String S3_ASSETS = "https://s3.amazonaws.com/mycollab_assets/%s";

		@Override
		protected Resource generateResource(String resourceId) {
			return new ExternalResource(String.format(S3_ASSETS, resourceId));
		}

		@Override
		protected String generateResourceLink(String resourceId) {
			return S3AssetsResource.generateResourceLink(resourceId);
		}

	}

	public static class VaadinThemeResource extends MyCollabResource {

		@Override
		protected Resource generateResource(String resourceId) {
			return new ExternalResource(generateResourceLink(resourceId));
		}

		@Override
		protected String generateResourceLink(String resourceId) {
			return AppContext.getSiteUrl() + "assets/" + resourceId;
		}

	}
}
