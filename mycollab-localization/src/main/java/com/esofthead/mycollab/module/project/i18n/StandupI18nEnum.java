package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/standup")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum StandupI18nEnum {
	VIEW_LIST_TITLE,
	CHOOSE_REPORT_DATE,
	BUTTON_ADD_REPORT_LABEL,
	
	STANDUP_LASTDAY,
	STANDUP_TODAY,
	STANDUP_ISSUE,
	
	STANDUP_MEMBER_NOT_REPORT,
	STANDUP_NO_ITEM,
	
	HINT1_MSG,
	HINT2_MG,
	
	FORM_EDIT_TITLE,
}
