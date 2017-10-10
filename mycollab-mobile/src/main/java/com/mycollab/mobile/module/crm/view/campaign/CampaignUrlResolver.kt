/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.campaign

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CampaignEvent
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.CampaignWithBLOBs

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CampaignUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", CampaignListUrlResolver())
        this.addSubResolver("add", CampaignAddUrlResolver())
        this.addSubResolver("edit", CampaignEditUrlResolver())
        this.addSubResolver("preview", CampaignPreviewUrlResolver())
    }

    class CampaignListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this,
                    CrmModuleScreenData.GotoModule(arrayOf())))
        }
    }

    class CampaignAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CampaignEvent.GotoAdd(this, CampaignWithBLOBs()))
    }

    class CampaignEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoEdit(this, campaignId))
        }
    }

    class CampaignPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoRead(this, campaignId))
        }
    }
}