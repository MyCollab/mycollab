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
package com.mycollab.module.crm.domain.criteria;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.db.query.*;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_priority = CacheParamMapper.register(CrmTypeConstants.CASE, CaseI18nEnum.FORM_PRIORITY,
            new StringListParam("priority", "m_crm_case", "priority", Arrays.asList(CrmDataTypeFactory.getCasesPriorityList())));

    public static final Param p_account = CacheParamMapper.register(CrmTypeConstants.CASE, CaseI18nEnum.FORM_ACCOUNT,
            new PropertyParam("account", "m_crm_case", "accountId"));

    public static final Param p_status = CacheParamMapper.register(CrmTypeConstants.CASE, GenericI18Enum.FORM_STATUS,
            new StringListParam("status", "m_crm_case", "status", Arrays.asList(CrmDataTypeFactory.getCasesStatusList())));

    public static final Param p_type = CacheParamMapper.register(CrmTypeConstants.CASE, GenericI18Enum.FORM_TYPE,
            new StringListParam("type", "m_crm_case", "type", Arrays.asList(CrmDataTypeFactory.getCasesType())));

    public static final Param p_reason = CacheParamMapper.register(CrmTypeConstants.CASE, CaseI18nEnum.FORM_REASON,
            new StringListParam("reason", "m_crm_case", "reason", Arrays.asList(CrmDataTypeFactory.getCasesReason())));

    public static final Param p_origin = CacheParamMapper.register(CrmTypeConstants.CASE, CaseI18nEnum.FORM_ORIGIN,
            new StringListParam("origin", "m_crm_case", "origin", Arrays.asList(CrmDataTypeFactory.getCasesOrigin())));

    public static final Param p_subject = CacheParamMapper.register(CrmTypeConstants.CASE, CaseI18nEnum.FORM_SUBJECT,
            new StringParam("subject", "m_crm_case", "subject"));

    public static final Param p_email = CacheParamMapper.register(CrmTypeConstants.CASE, CaseI18nEnum.FORM_EMAIL,
            new StringParam("email", "m_crm_case", "email"));

    public static final Param p_assignee = CacheParamMapper.register(CrmTypeConstants.CASE, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam("assignuser", "m_crm_case", "assignUser"));

    public static final Param p_createdtime = CacheParamMapper.register(CrmTypeConstants.CASE, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdtime", "m_crm_case", "createdTime"));

    public static final Param p_lastupdatedtime = CacheParamMapper.register(CrmTypeConstants.CASE, GenericI18Enum.FORM_LAST_UPDATED_TIME,
            new DateParam("lastupdatedtime", "m_crm_case", "lastUpdatedTime"));

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
