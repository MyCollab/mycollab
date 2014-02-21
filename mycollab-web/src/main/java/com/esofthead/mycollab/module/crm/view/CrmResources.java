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

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmResources {
	private static final Map<String, String> resourceLinks;

	static {
		resourceLinks = new HashMap<String, String>();
		resourceLinks.put(CrmTypeConstants.ACCOUNT,
				MyCollabResource.newResourceLink("icons/16/crm/account.png"));
		resourceLinks.put(CrmTypeConstants.CALL,
				MyCollabResource.newResourceLink("icons/16/crm/call.png"));
		resourceLinks.put(CrmTypeConstants.CAMPAIGN,
				MyCollabResource.newResourceLink("icons/16/crm/campaign.png"));
		resourceLinks.put(CrmTypeConstants.CASE,
				MyCollabResource.newResourceLink("icons/16/crm/case.png"));
		resourceLinks.put(CrmTypeConstants.CONTACT,
				MyCollabResource.newResourceLink("icons/16/crm/contact.png"));
		resourceLinks.put(CrmTypeConstants.LEAD,
				MyCollabResource.newResourceLink("icons/16/crm/lead.png"));
		resourceLinks.put(CrmTypeConstants.MEETING,
				MyCollabResource.newResourceLink("icons/16/crm/meeting.png"));
		resourceLinks.put(CrmTypeConstants.OPPORTUNITY, MyCollabResource
				.newResourceLink("icons/16/crm/opportunity.png"));
		resourceLinks.put(CrmTypeConstants.TASK,
				MyCollabResource.newResourceLink("icons/16/crm/task.png"));

	}

	public static String getResourceLink(String type) {
		return resourceLinks.get(type);
	}
}
