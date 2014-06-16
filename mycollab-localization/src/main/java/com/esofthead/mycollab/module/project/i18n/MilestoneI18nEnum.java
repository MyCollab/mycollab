package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/milestone")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum MilestoneI18nEnum {
	LIST_VIEW_TITLE,
	NEW_PHASE_ACTION,
	
	CLOSED_PHASE_TITLE,
	INPROGRESS_PHASE_TITLE,
	FUTURE_PHASE_TITLE,
	
	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_NAME_FIELD,
	FORM_START_DATE_FIELD,
	FORM_END_DATE_FIELD,
	FORM_STATUS_FIELD, 
	FORM_TASK_FIELD,
	FORM_BUG_FIELD,
	FORM_DESCRIPTION_FIELD,
	
	RELATED_TASKS_TAB,
	RELATED_BUGS_TAB,
	
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT
}
