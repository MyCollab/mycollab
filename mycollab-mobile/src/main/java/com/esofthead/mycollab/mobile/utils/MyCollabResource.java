package com.esofthead.mycollab.mobile.utils;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

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
			return String.format(S3_ASSETS, resourceId);
		}

	}

	public static class VaadinThemeResource extends MyCollabResource {

		@Override
		protected Resource generateResource(String resourceId) {
			return new ThemeResource(resourceId);
		}

		@Override
		protected String generateResourceLink(String resourceId) {
			return AppContext.getSiteUrl() + "assets/" + resourceId;
		}

	}
}
