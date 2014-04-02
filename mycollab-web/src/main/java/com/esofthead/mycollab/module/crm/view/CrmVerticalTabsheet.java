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


import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.vaadin.ui.Button;
public class CrmVerticalTabsheet extends VerticalTabsheet{
	/**
	 * 
	 */
	public CrmVerticalTabsheet(boolean isLeft) {
	super(isLeft);
	}
	private static final long serialVersionUID = 1L;

	@Override 
	protected void setDefaulButtonIcon(Button btn, Boolean selected) {
		String caption = btn.getCaption();
		String suffix;
		if (selected != true)
			suffix = "_white";
		else
			suffix = "";

		switch (caption){
		case "About":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/detail" + suffix + ".png"));
			break;

		case "Campaigns":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/campaign" + suffix + ".png"));
			break;
		case "Contacts":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/contact" + suffix + ".png"));
			break;

		case "Leads":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/lead" + suffix + ".png"));
			break;

		case "Opportunities":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/opportunity" + suffix + ".png"));
			break;

		case "Cases":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/case" + suffix + ".png"));
			break;
		case "Activities":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/activitylist" + suffix + ".png"));
			break;
			
		case "Events":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/event" + suffix + ".png"));
			break;
		case "Mettings":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/meeting" + suffix + ".png"));
			break;
		case "Notifications":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/notification" + suffix + ".png"));
			break;
		case "Custom Layouts":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/layout" + suffix + ".png"));
			break;
		case "Accounts":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/account" + suffix + ".png"));
			break;
			
		default:
			break;
			
		}

	}

}
