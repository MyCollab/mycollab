/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.campaign

import com.mycollab.common.UrlTokenizer
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.mobile.module.crm.events.{CampaignEvent, CrmEvent}
import com.mycollab.mobile.module.crm.{CrmModuleScreenData, CrmUrlResolver}
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.i18n.CampaignI18nEnum
import com.mycollab.vaadin.AppContext

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class CampaignUrlResolver extends CrmUrlResolver {
  this.addSubResolver("list", new CampaignListUrlResolver)
  this.addSubResolver("add", new CampaignAddUrlResolver)
  this.addSubResolver("edit", new CampaignEditUrlResolver)
  this.addSubResolver("preview", new CampaignPreviewUrlResolver)
  
  class CampaignListUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new CrmEvent.GotoContainer(this,
        new CrmModuleScreenData.GotoModule(AppContext.getMessage(CampaignI18nEnum.LIST))))
    }
  }
  
  class CampaignAddUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, new Account))
    }
  }
  
  class CampaignEditUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val campaignId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new CampaignEvent.GotoEdit(this, campaignId))
    }
  }
  
  class CampaignPreviewUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      val campaignId = UrlTokenizer(params(0)).getInt
      EventBusFactory.getInstance().post(new CampaignEvent.GotoRead(this, campaignId))
    }
  }
  
}
