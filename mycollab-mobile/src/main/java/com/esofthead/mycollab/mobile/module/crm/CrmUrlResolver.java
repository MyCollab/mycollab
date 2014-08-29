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
package com.esofthead.mycollab.mobile.module.crm;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountUrlResolver;
import com.esofthead.mycollab.mobile.shell.ModuleHelper;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.mvp.UrlResolver;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class CrmUrlResolver extends UrlResolver {

	public UrlResolver build() {
		this.addSubResolver("account", new AccountUrlResolver());
		// this.addSubResolver("contact", new ContactUrlResolver());
		// this.addSubResolver("campaign", new CampaignUrlResolver());
		// this.addSubResolver("lead", new LeadUrlResolver());
		// this.addSubResolver("opportunity", new OpportunityUrlResolver());
		// this.addSubResolver("cases", new CaseUrlResolver());
		// this.addSubResolver("activity", new ActivityUrlResolver());
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

}
