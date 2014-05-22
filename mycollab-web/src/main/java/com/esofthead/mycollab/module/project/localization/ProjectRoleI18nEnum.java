package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/role")
@LocaleData({ @Locale("en_US") })
public enum ProjectRoleI18nEnum {
	LIST_VIEW_TITLE,
	
	FORM_READ_TITLE,
	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_NAME,
	FORM_DESCRIPTION,
	PERMISSIONS_HEADER
}
