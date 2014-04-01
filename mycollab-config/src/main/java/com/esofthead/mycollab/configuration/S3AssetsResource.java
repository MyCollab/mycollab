package com.esofthead.mycollab.configuration;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class S3AssetsResource {
	private static String S3_ASSETS = "https://s3.amazonaws.com/mycollab_assets/%s";

	public static String generateResourceLink(String resourceId) {
		return String.format(S3_ASSETS, resourceId);
	}
}
