/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.mail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.esofthead.mycollab.configuration.LocaleHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.MyCollabException;
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
		return templatePath(path, LocaleHelper.toLocale(language));
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
}
