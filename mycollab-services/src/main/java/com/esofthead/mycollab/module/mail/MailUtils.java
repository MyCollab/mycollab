package com.esofthead.mycollab.module.mail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.i18n.LocaleUtils;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class MailUtils {

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

	private static Map<String, String> cacheFile = new HashMap<String, String>();

	public static String templatePath(String path, String language) {
		return templatePath(path, LocaleUtils.toLocale(language));
	}

	public static String templatePath(String path, Locale locale) {
		String key = (locale != null) ? (path + locale.toString())
				: (path + Locale.US.toString());
		String filePath = cacheFile.get(key);
		if (filePath != null) {
			return filePath;
		} else {
			int index = path.indexOf("mt");
			if (index == -1) {
				throw new MyCollabException("File type is not supported "
						+ path);
			}
			filePath = path.substring(0, index - 1);
			filePath = String.format("%s_%s.mt", filePath, locale);
			cacheFile.put(key, filePath);
			return filePath;
		}
	}

	public static void main(String[] args) {
		System.out.println(Locale.US);
	}
}
