package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/role")
@LocaleData({ @Locale("en_US") })
public enum ProjectRoleI18nEnum {
	VIEW_LIST_TITLE,
	
	FORM_READ_TITLE,
	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_NAME,
	
	SECTION_PERMISSIONS
}
