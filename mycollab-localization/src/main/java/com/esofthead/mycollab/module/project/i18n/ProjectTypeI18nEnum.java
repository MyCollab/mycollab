package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@BaseName("localization/project/type")
@LocaleData({ @Locale("en_US") })
public enum ProjectTypeI18nEnum {
	PROJECT_ITEM,
	MESSAGE_ITEM,
	PHASE_ITEM,
	TASK_ITEM,
	TASKGROUP_ITEM,
	BUG_ITEM,
	COMPONENT_ITEM,
	VERSION_ITEM,
	RISK_ITEM,
	PROBLEM_ITEM,
	STANDUP_ITEM
}
