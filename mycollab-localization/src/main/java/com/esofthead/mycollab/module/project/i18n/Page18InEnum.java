package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/page")
@LocaleData(value={ @Locale("en_US"), @Locale("ja_JP") }, defaultCharset="UTF-8")
public enum Page18InEnum {
	VIEW_LIST_TITLE,
	VIEW_NEW_TITLE,
	VIEW_READ_TITLE,
	VIEW_EDIT_TITLE,
	
	DIALOG_NEW_GROUP_TITLE,
	DIALOG_EDIT_GROUP_TITLE,
	
	BUTTON_NEW_GROUP,
	BUTTON_NEW_PAGE,
	
	FORM_GROUP,
	
	FORM_SUBJECT,
	FORM_DESCRIPTION,
	FORM_VISIBILITY,
	FORM_CATEGORY,
	
	OPT_CREATED_USER,
	OPT_SORT_LABEL,
	OPT_SORT_BY_DATE,
	OPT_SORT_BY_NAME,
	OPT_SORT_BY_KIND,
	
	LABEL_LAST_UPDATE
}
