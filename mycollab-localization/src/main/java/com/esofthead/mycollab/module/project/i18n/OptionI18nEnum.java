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

	public static BugPriority[] bug_priorities = { BugPriority.Blocker,
			BugPriority.Critical, BugPriority.Major, BugPriority.Minor,
			BugPriority.Trivial };

	public static BugSeverity[] bug_severities = { BugSeverity.Critical,
			BugSeverity.Major, BugSeverity.Minor, BugSeverity.Trivial };

	public static BugResolution[] bug_resolutions = { BugResolution.Fixed,
			BugResolution.Won_Fix, BugResolution.Duplicate,
			BugResolution.Incomplete, BugResolution.CannotReproduce,
			BugResolution.WaitforVerification, BugResolution.Newissue,
			BugResolution.ReOpen };

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

	@BaseName("localization/project/bug_priority")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public static enum BugPriority {
		Blocker, Critical, Major, Minor, Trivial
	}

	@BaseName("localization/project/bug_severity")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public static enum BugSeverity {
		Critical, Major, Minor, Trivial
	}

	@BaseName("localization/project/bug_resolution")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public static enum BugResolution {
		Fixed,
		Won_Fix,
		Duplicate,
		Incomplete,
		CannotReproduce,
		WaitforVerification,
		Newissue,
		ReOpen
	}
}
