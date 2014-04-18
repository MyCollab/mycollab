package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.module.tracker.BugResolutionConstants;
import com.esofthead.mycollab.module.tracker.BugStatusConstants;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectDataTypeFactory {

	public static final String PROJECT_STATUS_OPEN = "Open";
	public static final String PROJECT_STATUS_CLOSED = "Closed";

	private static final String[] PROJECT_STATUSES_LIST = new String[] {
			"Open", "Closed" };

	private static String[] PROJECT_TYPE_LIST = new String[] { "Unknown",
			"Administrative", "Operative" };

	private static String[] TASK_PRIORITY_LIST = new String[] {
			TaskPriorityStatusContants.PRIORITY_URGENT,
			TaskPriorityStatusContants.PRIORITY_HIGHT,
			TaskPriorityStatusContants.PRIORITY_MEDIUM,
			TaskPriorityStatusContants.PRIORITY_NONE };

	private static String[] BUG_PRIORITY_LIST = new String[] {
			BugPriorityStatusConstants.BLOCKER,
			BugPriorityStatusConstants.CRITICAL,
			BugPriorityStatusConstants.MAJOR,
			BugPriorityStatusConstants.MINOR,
			BugPriorityStatusConstants.TRIVIAL };

	private static String[] BUG_SEVERITY_LIST = new String[] { "Critical",
			"Major", "Minor", "Trivial" };

	private static String[] BUG_STATUS_LIST = new String[] {
			BugStatusConstants.VERIFIED, BugStatusConstants.INPROGRESS,
			BugStatusConstants.OPEN, BugStatusConstants.RESOLVED };

	private static String[] BUG_RESOLUTION_LIST = new String[] {
			BugResolutionConstants.CAN_NOT_REPRODUCE,
			BugResolutionConstants.DUPLICATE, BugResolutionConstants.REOPEN,
			BugResolutionConstants.FIXED, BugResolutionConstants.INCOMPLETE,
			BugResolutionConstants.NEWISSUE,
			BugResolutionConstants.WAITFORVERIFICATION,
			BugResolutionConstants.WON_FIX };

	public static String[] getProjectTypeList() {
		return PROJECT_TYPE_LIST;
	}

	public static String[] getTaskPriorityList() {
		return TASK_PRIORITY_LIST;
	}

	public static String[] getBugPriorityList() {
		return BUG_PRIORITY_LIST;
	}

	public static String[] getBugStatusList() {
		return BUG_STATUS_LIST;
	}

	public static String[] getProjectStatusList() {
		return PROJECT_STATUSES_LIST;
	}

	public static String[] getBugSeverityList() {
		return BUG_SEVERITY_LIST;
	}

	public static String[] getBugResolutionList() {
		return BUG_RESOLUTION_LIST;
	}
}
