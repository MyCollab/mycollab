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
package com.esofthead.mycollab.module.crm.view;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.events.CrmEvent;
import com.esofthead.mycollab.module.crm.view.account.AccountUrlResolver;
import com.esofthead.mycollab.module.crm.view.activity.ActivityUrlResolver;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignUrlResolver;
import com.esofthead.mycollab.module.crm.view.cases.CaseUrlResolver;
import com.esofthead.mycollab.module.crm.view.contact.ContactUrlResolver;
import com.esofthead.mycollab.module.crm.view.file.FileUrlResolver;
import com.esofthead.mycollab.module.crm.view.lead.LeadUrlResolver;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityUrlResolver;
import com.esofthead.mycollab.module.crm.view.setting.CrmSettingUrlResolver;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.desktop.ui.ModuleHelper;
import com.esofthead.mycollab.vaadin.desktop.ui.UrlResolver;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmUrlResolver extends UrlResolver {
	public UrlResolver build() {
		this.addSubResolver("dashboard", new CrmDashboardUrlResolver());
		this.addSubResolver("account", new AccountUrlResolver());
		this.addSubResolver("contact", new ContactUrlResolver());
		this.addSubResolver("campaign", new CampaignUrlResolver());
		this.addSubResolver("lead", new LeadUrlResolver());
		this.addSubResolver("opportunity", new OpportunityUrlResolver());
		this.addSubResolver("cases", new CaseUrlResolver());
		this.addSubResolver("activity", new ActivityUrlResolver());
		this.addSubResolver("file", new FileUrlResolver());
		this.addSubResolver("setting", new CrmSettingUrlResolver());
		return this;
	}

	@Override
	public void handle(String... params) {
		if (!ModuleHelper.isCurrentCrmModule()) {
			EventBusFactory.getInstance().post(
					new ShellEvent.GotoCrmModule(this, params));
		} else {
			super.handle(params);
		}
	}

	@Override
	protected void defaultPageErrorHandler() {
		EventBusFactory.getInstance().post(
				new ShellEvent.GotoCrmModule(this, null));

	}

	public static class CrmDashboardUrlResolver extends CrmUrlResolver {

		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
		}
	}
}
