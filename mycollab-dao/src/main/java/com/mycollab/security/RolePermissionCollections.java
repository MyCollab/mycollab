/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.security;

import com.google.common.collect.ImmutableList;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;

import java.util.List;

/**
 * Keep all permissions of MyCollab
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RolePermissionCollections {
    public static final String CRM_ACCOUNT = "Account";
    public static final String CRM_CONTACT = "Contact";
    public static final String CRM_CAMPAIGN = "Campaign";
    public static final String CRM_LEAD = "Lead";
    public static final String CRM_OPPORTUNITY = "Opportunity";
    public static final String CRM_CASE = "Case";
    public static final String CRM_TASK = "Task";
    public static final String CRM_MEETING = "Meeting";
    public static final String CRM_CALL = "Call";
    public static final String CRM_DOCUMENT = "Document";

    public static final String ACCOUNT_USER = "User";
    public static final String ACCOUNT_ROLE = "Role";
    public static final String ACCOUNT_BILLING = "Billing";
    public static final String ACCOUNT_THEME = "Theme";

    public static final String CREATE_NEW_PROJECT = "CreateNewProject";
    public static final String GLOBAL_PROJECT_SETTINGS = "GlobalProjectSettings";

    public static final String PUBLIC_DOCUMENT_ACCESS = "PublicDocumentAccess";

    public static final List<PermissionDefItem> CRM_PERMISSIONS_ARR = ImmutableList.of(
            new PermissionDefItem(CRM_ACCOUNT, AccountI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CONTACT, ContactI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CAMPAIGN, CampaignI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_LEAD, LeadI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_OPPORTUNITY, OpportunityI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CASE, CaseI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_TASK, TaskI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_MEETING, MeetingI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CALL, CallI18nEnum.SINGLE, AccessPermissionFlag.class));

    public static final List<PermissionDefItem> ACCOUNT_PERMISSION_ARR = ImmutableList.of(
            new PermissionDefItem(ACCOUNT_USER, UserI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(ACCOUNT_ROLE, RoleI18nEnum.SINGLE, AccessPermissionFlag.class),
            new PermissionDefItem(ACCOUNT_BILLING, RoleI18nEnum.OPT_BILLING_MANAGEMENT, BooleanPermissionFlag.class),
            new PermissionDefItem(ACCOUNT_THEME, RoleI18nEnum.OPT_THEME, BooleanPermissionFlag.class));

    public static final List<PermissionDefItem> PROJECT_PERMISSION_ARR = ImmutableList.of(new PermissionDefItem(
            CREATE_NEW_PROJECT, RoleI18nEnum.OPT_CREATE_NEW_PROJECT, BooleanPermissionFlag.class), new PermissionDefItem(
            GLOBAL_PROJECT_SETTINGS, RoleI18nEnum.OPT_GLOBAL_PROJECT_SETTINGS, BooleanPermissionFlag.class));

    public static final List<PermissionDefItem> DOCUMENT_PERMISSION_ARR = ImmutableList.of(new PermissionDefItem(
            PUBLIC_DOCUMENT_ACCESS, RoleI18nEnum.OPT_PUBLIC_DOCUMENTS, AccessPermissionFlag.class));
}
