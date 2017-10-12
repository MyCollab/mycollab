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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.campaign

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.event.CampaignEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CampaignUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    class ListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CampaignEvent.GotoList(this, null))
    }

    class AddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CampaignEvent.GotoAdd(this, Account()))
    }

    class EditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoEdit(this, campaignId))
        }
    }

    class PreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoRead(this, campaignId))
        }
    }
}