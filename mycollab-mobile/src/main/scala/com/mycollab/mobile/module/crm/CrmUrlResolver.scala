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
package com.mycollab.mobile.module.crm

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.mobile.module.crm.events.CrmEvent
import com.mycollab.mobile.module.crm.view.account.AccountUrlResolver
import com.mycollab.mobile.module.crm.view.activity.ActivityUrlResolver
import com.mycollab.mobile.module.crm.view.campaign.CampaignUrlResolver
import com.mycollab.mobile.module.crm.view.cases.CaseUrlResolver
import com.mycollab.mobile.module.crm.view.contact.ContactUrlResolver
import com.mycollab.mobile.module.crm.view.lead.LeadUrlResolver
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityUrlResolver
import com.mycollab.mobile.shell.ModuleHelper
import com.mycollab.mobile.shell.events.ShellEvent
import com.mycollab.vaadin.mvp.UrlResolver

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class CrmUrlResolver extends UrlResolver {
  def build: UrlResolver = {
    this.addSubResolver("dashboard", new DashboardUrlResolver)
    this.addSubResolver("account", new AccountUrlResolver)
    this.addSubResolver("contact", new ContactUrlResolver)
    this.addSubResolver("campaign", new CampaignUrlResolver)
    this.addSubResolver("lead", new LeadUrlResolver)
    this.addSubResolver("opportunity", new OpportunityUrlResolver)
    this.addSubResolver("cases", new CaseUrlResolver)
    this.addSubResolver("activity", new ActivityUrlResolver)
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

  protected override def handlePage(params: String*) {
    super.handlePage(params: _*)
    EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null))
  }

  protected def defaultPageErrorHandler() {
    EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null))
  }

  class DashboardUrlResolver extends CrmUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new CrmEvent.GotoActivitiesView(this, null))
    }
  }

}
