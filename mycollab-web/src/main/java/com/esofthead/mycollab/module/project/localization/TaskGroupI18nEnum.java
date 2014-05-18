package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/taskgroup")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum TaskGroupI18nEnum {
	FORM_NEW_TASKGROUP_TITLE,
	FORM_EDIT_TASKGROUP_TITLE,
	FORM_VIEW_TASKGROUP_TITLE,
	
	FORM_NAME_FIELD,
	FORM_DESCRIPTION_FIELD,
	FORM_MILESTONE_FIELD,
	FORM_PROGRESS_FIELD,
	FORM_OPEN_TASKS_FIELD,
	
	FILTER_ALL_TASK_GROUPS_TITLE,
	FILTER_ACTIVE_TASK_GROUPS_TITLE,
	FILTER_ARCHIEVED_TASK_GROUPS_TITLE,
	
	FILTER_ALL_TASKS,
	FILTER_ACTIVE_TASKS,
	FILTER_PENDING_TASKS,
	FILTER_ARCHIEVED_TASKS,
	
	TASKS_TAB
}
