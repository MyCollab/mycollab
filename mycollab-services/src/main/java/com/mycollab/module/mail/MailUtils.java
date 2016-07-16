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
package com.mycollab.module.mail;

import com.mycollab.configuration.IDeploymentMode;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.module.user.domain.BillingAccount;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.spring.AppContextUtil;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class MailUtils {
    public static String getSiteUrl(Integer sAccountId) {
        String siteUrl = "";
        IDeploymentMode mode = AppContextUtil.getSpringBean(IDeploymentMode.class);
        if (mode.isDemandEdition()) {
            BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);
            BillingAccount account = billingAccountService.getAccountById(sAccountId);
            if (account != null) {
                siteUrl = SiteConfiguration.getSiteUrl(account.getSubdomain());
            }
        } else {
            siteUrl = SiteConfiguration.getSiteUrl("");
        }
        return siteUrl;
    }

    public static String getAvatarLink(String userAvatarId, int size) {
        return StorageFactory.getAvatarPath(userAvatarId, size);
    }
}
