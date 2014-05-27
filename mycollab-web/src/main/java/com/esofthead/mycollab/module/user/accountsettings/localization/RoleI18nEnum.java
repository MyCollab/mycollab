package com.esofthead.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/accountsettings/role")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum RoleI18nEnum {
	LIST_VIEW_TITLE,
	
	PERMISSION_HEADER,
	PROJECT_MANAGEMENT_TITLE,
	CRM_TITLE,
	DOCUMENT_TITLE,
	ACCOUNT_MANAGEMENT_TITLE,
	
	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_NAME_FIELD,
	FORM_DESCRIPTION_FIELD
}
