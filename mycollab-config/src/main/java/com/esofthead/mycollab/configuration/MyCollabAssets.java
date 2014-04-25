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
		if (deploymentMode == DeploymentMode.SITE) {
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
							.getServerPort())
					+ "assets/" + resourceId;
		}

	}
}
