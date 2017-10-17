/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin

import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.Version
import com.mycollab.module.file.StorageUtils
import com.mycollab.module.user.service.BillingAccountService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUIProvider.Companion.MOBILE_APP
import com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID
import com.vaadin.server.BootstrapFragmentResponse
import com.vaadin.server.BootstrapListener
import com.vaadin.server.BootstrapPageResponse

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class AppBootstrapListener : BootstrapListener {

    override fun modifyBootstrapFragment(response: BootstrapFragmentResponse) {}

    override fun modifyBootstrapPage(response: BootstrapPageResponse) {
        val request = response.request
        val domain = Utils.getSubDomain(request)
        val billingService = AppContextUtil.getSpringBean(BillingAccountService::class.java)

        val account = billingService.getAccountByDomain(domain)
        if (account != null) {
            val favIconPath = StorageUtils.getFavIconPath(account.id!!, account.faviconpath)
            response.document.head().getElementsByAttributeValue("rel", "shortcut icon").attr("href", favIconPath)
            response.document.head().getElementsByAttributeValue("rel", "icon").attr("href", favIconPath)
        }

        response.document.head().append("<meta name=\"robots\" content=\"nofollow\" />")

        val deploymentMode = AppContextUtil.getSpringBean(IDeploymentMode::class.java)
        if (MOBILE_APP != response.uiClass.name || !Utils.isTablet(request)) {
            response.document.head()
                    .append("<script type=\"text/javascript\" src=\"${deploymentMode.getCdnUrl()}js/jquery-2.1.4.min.js\"></script>")
            response.document.head()
                    .append("<script type=\"text/javascript\" src=\"${deploymentMode.getCdnUrl()}js/stickytooltip-1.0.2.js?v=${Version.getVersion()}\"></script>")

            val div1 = response.document.body().appendElement("div")
            div1.attr("id", "div1$TOOLTIP_ID")
            div1.attr("class", "stickytooltip")

            val div12 = div1.appendElement("div")
            div12.attr("style", "padding:5px")

            val div13 = div12.appendElement("div")
            div13.attr("id", "div13$TOOLTIP_ID")
            div13.attr("class", "atip")
            div13.attr("style", "width:550px")

            val div14 = div13.appendElement("div")
            div14.attr("id", "div14$TOOLTIP_ID")
        }
    }
}