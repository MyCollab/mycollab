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
package com.esofthead.mycollab.module.crm.domain.criteria;

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CaseSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_priority = new StringListParam("case-priority",
			"Priority", "m_crm_case", "priority",
			Arrays.asList(CrmDataTypeFactory.getCasesPriorityList()));

	public static Param p_status = new StringListParam("case-status", "Status",
			"m_crm_case", "status", Arrays.asList(CrmDataTypeFactory
					.getCasesStatusList()));

	public static Param p_type = new StringListParam("case-type", "Type",
			"m_crm_case", "type", Arrays.asList(CrmDataTypeFactory
					.getCasesType()));

	public static Param p_reason = new StringListParam("case-reason", "Reason",
			"m_crm_case", "reason", Arrays.asList(CrmDataTypeFactory
					.getCasesReason()));

	public static Param p_origin = new StringListParam("case-origin", "Origin",
			"m_crm_case", "origin", Arrays.asList(CrmDataTypeFactory
					.getCasesOrigin()));

	public static Param p_subject = new StringParam("case-subject", "Subject",
			"m_crm_case", "subject");

	public static Param p_email = new StringParam("case-subject", "Email",
			"m_crm_case", "email");

	public static Param p_assignee = new PropertyListParam("case-assignuser",
			"Assignee", "m_crm_case", "assignUser");

	private StringSearchField subject;

	private StringSearchField assignUser;

	private NumberSearchField accountId;

	private NumberSearchField contactId;

	private SetSearchField<String> statuses;

	private SetSearchField<String> priorities;

	private SetSearchField<String> assignUsers;

	private NumberSearchField id;

	public StringSearchField getSubject() {
		return subject;
	}

	public void setSubject(StringSearchField subject) {
		this.subject = subject;
	}

	public StringSearchField getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(StringSearchField assignUser) {
		this.assignUser = assignUser;
	}

	public NumberSearchField getAccountId() {
		return accountId;
	}

	public void setAccountId(NumberSearchField accountId) {
		this.accountId = accountId;
	}

	public NumberSearchField getContactId() {
		return contactId;
	}

	public void setContactId(NumberSearchField contactId) {
		this.contactId = contactId;
	}

	public SetSearchField<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(SetSearchField<String> statuses) {
		this.statuses = statuses;
	}

	public SetSearchField<String> getPriorities() {
		return priorities;
	}

	public void setPriorities(SetSearchField<String> priorities) {
		this.priorities = priorities;
	}

	public SetSearchField<String> getAssignUsers() {
		return assignUsers;
	}

	public void setAssignUsers(SetSearchField<String> assignUsers) {
		this.assignUsers = assignUsers;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public NumberSearchField getId() {
		return id;
	}
}
