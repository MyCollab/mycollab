package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/problem")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum ProblemI18nEnum {
	LIST_VIEW_TITLE,
	NEW_PROBLEM_ACTION,
	
	FORM_READ_TITLE,
	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_NAME,
	FORM_DESCRIPTION,
	FORM_RAISED_BY,
	FORM_DATE_DUE,
	FORM_STATUS,
	FORM_IMPACT,
	FORM_PRIORITY,
	FORM_RATING,
	FORM_RELATED,
	FORM_RESOLUTION,
	
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT
}
