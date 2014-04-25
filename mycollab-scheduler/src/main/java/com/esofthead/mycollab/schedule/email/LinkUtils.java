package com.esofthead.mycollab.schedule.email;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class LinkUtils {

	public static String getSiteUrl(Integer sAccountId) {
		String siteUrl = "";
		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.SITE) {
			BillingAccountService billingAccountService = ApplicationContextUtil
					.getSpringBean(BillingAccountService.class);
			BillingAccount account = billingAccountService
					.getAccountById(sAccountId);
			if (account != null) {
				siteUrl = SiteConfiguration.getSiteUrl(account.getSubdomain());
			}
		} else {
			siteUrl = SiteConfiguration.getSiteUrl("");
		}
		return siteUrl;
	}

	public static String getAvatarLink(String userAvatarId, int size) {
		return SiteConfiguration.getStorageConfiguration().generateAvatarPath(
				userAvatarId, size);
	}
}
