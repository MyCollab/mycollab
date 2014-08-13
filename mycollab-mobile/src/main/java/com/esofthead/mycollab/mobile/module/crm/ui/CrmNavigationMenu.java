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
package com.esofthead.mycollab.mobile.module.crm.ui;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.AccountEvent;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.mobile.module.crm.events.CaseEvent;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.mobile.ui.AbstractNavigationMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CrmNavigationMenu extends AbstractNavigationMenu {
	private static final long serialVersionUID = 1L;

	public CrmNavigationMenu() {
		super();

		// setWidth("100%");
		//
		// UserPanel userPanel = new UserPanel();
		// userPanel.setWidth("100%");
		// addComponent(userPanel);

		final MenuButton accountBtn = new MenuButton("Accounts", "&#xE601;");
		addMenu(accountBtn);

		final MenuButton contactBtn = new MenuButton("Contacts", "&#xE603;");
		addMenu(contactBtn);

		final MenuButton campaignBtn = new MenuButton("Campaigns", "&#xE602;");
		addMenu(campaignBtn);

		final MenuButton leadBtn = new MenuButton("Leads", "&#xE609;");
		addMenu(leadBtn);

		final MenuButton opportunityBtn = new MenuButton("Opportunities",
				"&#xE604;");
		addMenu(opportunityBtn);

		final MenuButton caseBtn = new MenuButton("Cases", "&#xE605;");
		addMenu(caseBtn);

		final MenuButton activityBtn = new MenuButton("Activities", "&#xE606;");
		addMenu(activityBtn);

		// final MenuButton documentBtn = new MenuButton("Documents",
		// "&#xE607;");
		// addComponent(documentBtn);
		// buttonMap.put("Documents", documentBtn);
		//
		// final MenuButton settingBtn = new MenuButton("Settings", "&#xE608;");
		// addComponent(settingBtn);
		// buttonMap.put("Settings", settingBtn);
	}

	@Override
	protected Button.ClickListener createDefaultButtonClickListener() {

		return new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final String caption = ((MenuButton) event.getButton())
						.getBtnId();

				if ("Accounts".equals(caption)) {
					EventBusFactory.getInstance().post(
							new AccountEvent.GotoList(this, null));
				} else if ("Activities".equals(caption)) {
					EventBusFactory.getInstance().post(
							new ActivityEvent.GotoList(this, null));
				} else if ("Contacts".equals(caption)) {
					EventBusFactory.getInstance().post(
							new ContactEvent.GotoList(this, null));
				} else if ("Campaigns".equals(caption)) {
					EventBusFactory.getInstance().post(
							new CampaignEvent.GotoList(this, null));
				} else if ("Cases".equals(caption)) {
					EventBusFactory.getInstance().post(
							new CaseEvent.GotoList(this, null));
				} else if ("Leads".equals(caption)) {
					EventBusFactory.getInstance().post(
							new LeadEvent.GotoList(this, null));
				} else if ("Opportunities".equals(caption)) {
					EventBusFactory.getInstance().post(
							new OpportunityEvent.GotoList(this, null));
				}

				/*
				 * for (final Iterator<MenuButton> it = CrmNavigationMenu.this
				 * .buttonIterator(); it.hasNext();) { final MenuButton btn =
				 * it.next(); btn.removeStyleName("isSelected"); }
				 * 
				 * event.getButton().addStyleName("isSelected");
				 */
			}
		};
	}
}
