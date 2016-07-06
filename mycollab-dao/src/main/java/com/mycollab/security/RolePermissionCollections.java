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
            new PermissionDefItem(CRM_ACCOUNT, "Account", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CONTACT, "Contact", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CAMPAIGN, "Campaign", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_LEAD, "Lead", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_OPPORTUNITY, "Opportunity", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CASE, "Case", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_TASK, "Task", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_MEETING, "Meeting", AccessPermissionFlag.class),
            new PermissionDefItem(CRM_CALL, "Call", AccessPermissionFlag.class));

    public static final List<PermissionDefItem> ACCOUNT_PERMISSION_ARR = ImmutableList.of(
            new PermissionDefItem(ACCOUNT_USER, "User", AccessPermissionFlag.class),
            new PermissionDefItem(ACCOUNT_ROLE, "Role", AccessPermissionFlag.class),
            new PermissionDefItem(ACCOUNT_BILLING, "Billing Management", BooleanPermissionFlag.class),
            new PermissionDefItem(ACCOUNT_THEME, "Theme", BooleanPermissionFlag.class));

    public static final List<PermissionDefItem> PROJECT_PERMISSION_ARR = ImmutableList.of(new PermissionDefItem(
            CREATE_NEW_PROJECT, "Create New Project", BooleanPermissionFlag.class), new PermissionDefItem(
            GLOBAL_PROJECT_SETTINGS, "Global Project Settings", BooleanPermissionFlag.class));

    public static final List<PermissionDefItem> DOCUMENT_PERMISSION_ARR = ImmutableList.of(new PermissionDefItem(
            PUBLIC_DOCUMENT_ACCESS, "Public Documents", AccessPermissionFlag.class));
}
