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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.PropertyParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CaseSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_priority = new StringListParam("case-priority",
			CaseI18nEnum.FORM_PRIORITY, "m_crm_case", "priority",
			Arrays.asList(CrmDataTypeFactory.getCasesPriorityList()));

	public static Param p_account = new PropertyParam("case-account",
			CaseI18nEnum.FORM_ACCOUNT, "m_crm_case", "accountId");

	public static Param p_status = new StringListParam("case-status",
			CaseI18nEnum.FORM_STATUS, "m_crm_case", "status",
			Arrays.asList(CrmDataTypeFactory.getCasesStatusList()));

	public static Param p_type = new StringListParam("case-type",
			CaseI18nEnum.FORM_TYPE, "m_crm_case", "type",
			Arrays.asList(CrmDataTypeFactory.getCasesType()));

	public static Param p_reason = new StringListParam("case-reason",
			CaseI18nEnum.FORM_REASON, "m_crm_case", "reason",
			Arrays.asList(CrmDataTypeFactory.getCasesReason()));

	public static Param p_origin = new StringListParam("case-origin",
			CaseI18nEnum.FORM_ORIGIN, "m_crm_case", "origin",
			Arrays.asList(CrmDataTypeFactory.getCasesOrigin()));

	public static Param p_subject = new StringParam("case-subject",
			CaseI18nEnum.FORM_SUBJECT, "m_crm_case", "subject");

	public static Param p_email = new StringParam("case-subject",
			CaseI18nEnum.FORM_EMAIL, "m_crm_case", "email");

	public static Param p_assignee = new PropertyListParam("case-assignuser",
			GenericI18Enum.FORM_ASSIGNEE, "m_crm_case", "assignUser");

	public static Param p_createdtime = new DateParam("case-createdtime",
			GenericI18Enum.FORM_CREATED_TIME, "m_crm_case", "createdTime");

	public static Param p_lastupdatedtime = new DateParam(
			"case-lastupdatedtime", GenericI18Enum.FORM_LAST_UPDATED_TIME,
			"m_crm_case", "lastUpdatedTime");

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
