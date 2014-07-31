package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class OptionI18nEnum {
	public static TaskPriority[] task_priorities = { TaskPriority.Urgent,
			TaskPriority.High, TaskPriority.Medium, TaskPriority.Low,
			TaskPriority.None };

	public static BugStatus[] bug_statuses = { BugStatus.Open,
			BugStatus.InProgress, BugStatus.Verified, BugStatus.Resolved,
			BugStatus.ReOpened };

	@BaseName("localization/project/milestone_status")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public static enum MilestoneStatus {
		Future, Closed, InProgress
	}

	@BaseName("localization/project/task_priority")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public static enum TaskPriority {
		Urgent, High, Medium, Low, None
	}

	@BaseName("localization/project/bug_status")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public static enum BugStatus {
		Open, InProgress, Verified, Resolved, ReOpened
	}
}
