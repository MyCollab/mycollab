/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.campaign

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.crm.events.CampaignEvent
import com.mycollab.module.crm.view.CrmUrlResolver
import com.mycollab.common.UrlTokenizer
import com.mycollab.module.crm.domain.Account

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class CampaignUrlResolver extends CrmUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)

  class ListUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new CampaignEvent.GotoList(this, null))
    }
  }

  class AddUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, new Account))
    }
  }

  class EditUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val campaignId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new CampaignEvent.GotoEdit(this, campaignId))
    }
  }

  class PreviewUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val campaignId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new CampaignEvent.GotoRead(this, campaignId))
    }
  }

}
