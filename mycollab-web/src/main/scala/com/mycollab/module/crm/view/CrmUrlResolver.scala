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
package com.mycollab.module.crm.view

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.crm.events.CrmEvent
import com.mycollab.module.crm.view.account.AccountUrlResolver
import com.mycollab.module.crm.view.activity.ActivityUrlResolver
import com.mycollab.module.crm.view.campaign.CampaignUrlResolver
import com.mycollab.module.crm.view.cases.CaseUrlResolver
import com.mycollab.module.crm.view.contact.ContactUrlResolver
import com.mycollab.module.crm.view.file.FileUrlResolver
import com.mycollab.module.crm.view.lead.LeadUrlResolver
import com.mycollab.module.crm.view.opportunity.OpportunityUrlResolver
import com.mycollab.module.crm.view.setting.CrmSettingUrlResolver
import com.mycollab.shell.events.ShellEvent
import com.mycollab.vaadin.mvp.UrlResolver
import com.mycollab.vaadin.web.ui.ModuleHelper

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class CrmUrlResolver extends UrlResolver {
  def build: UrlResolver = {
    this.addSubResolver("dashboard", new CrmDashboardUrlResolver)
    this.addSubResolver("account", new AccountUrlResolver)
    this.addSubResolver("contact", new ContactUrlResolver)
    this.addSubResolver("campaign", new CampaignUrlResolver)
    this.addSubResolver("lead", new LeadUrlResolver)
    this.addSubResolver("opportunity", new OpportunityUrlResolver)
    this.addSubResolver("cases", new CaseUrlResolver)
    this.addSubResolver("activity", new ActivityUrlResolver)
    this.addSubResolver("file", new FileUrlResolver)
    this.addSubResolver("setting", new CrmSettingUrlResolver)
    this
  }

  override def handle(params: String*) {
    if (!ModuleHelper.isCurrentCrmModule) {
      EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, params))
    }
    else {
      super.handle(params: _*)
    }
  }

  protected def defaultPageErrorHandler() {
    EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null))
  }

  class CrmDashboardUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null))
    }
  }

}
