/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm;

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.configuration.MyCollabAssets;

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
				MyCollabAssets.newResourceLink("icons/16/crm/account.png"));
		resourceLinks.put(CrmTypeConstants.CALL,
				MyCollabAssets.newResourceLink("icons/16/crm/call.png"));
		resourceLinks.put(CrmTypeConstants.CAMPAIGN,
				MyCollabAssets.newResourceLink("icons/16/crm/campaign.png"));
		resourceLinks.put(CrmTypeConstants.CASE,
				MyCollabAssets.newResourceLink("icons/16/crm/case.png"));
		resourceLinks.put(CrmTypeConstants.CONTACT,
				MyCollabAssets.newResourceLink("icons/16/crm/contact.png"));
		resourceLinks.put(CrmTypeConstants.LEAD,
				MyCollabAssets.newResourceLink("icons/16/crm/lead.png"));
		resourceLinks.put(CrmTypeConstants.MEETING,
				MyCollabAssets.newResourceLink("icons/16/crm/meeting.png"));
		resourceLinks.put(CrmTypeConstants.OPPORTUNITY,
				MyCollabAssets.newResourceLink("icons/16/crm/opportunity.png"));
		resourceLinks.put(CrmTypeConstants.TASK,
				MyCollabAssets.newResourceLink("icons/16/crm/task.png"));

	}

	public static String getResourceLink(String type) {
		return resourceLinks.get(type);
	}
}
