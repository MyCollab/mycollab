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
package com.mycollab.module.mail

import com.mycollab.configuration.{IDeploymentMode, SiteConfiguration, StorageFactory}
import com.mycollab.module.user.service.BillingAccountService
import com.mycollab.spring.AppContextUtil

/**
  * @author MyCollab Ltd
  * @since 5.4.1
  */
object MailUtils {
  def getSiteUrl(sAccountId: Integer): String = {
    var siteUrl = ""
    val mode = AppContextUtil.getSpringBean(classOf[IDeploymentMode])
    if (mode.isDemandEdition) {
      val billingAccountService: BillingAccountService = AppContextUtil.getSpringBean(classOf[BillingAccountService])
      val account = billingAccountService.getAccountById(sAccountId)
      if (account != null) siteUrl = SiteConfiguration.getSiteUrl(account.getSubdomain)
    }
    else siteUrl = SiteConfiguration.getSiteUrl("")
    siteUrl
  }
  
  def getAvatarLink(userAvatarId: String, size: Int): String = StorageFactory.getAvatarPath(userAvatarId, size)
}
