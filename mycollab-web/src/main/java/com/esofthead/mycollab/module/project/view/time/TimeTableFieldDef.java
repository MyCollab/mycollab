package com.esofthead.mycollab.module.project.view.time;

import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TimeTableFieldDef {
	public static TableViewField id = new TableViewField("", "id", 60);

	public static TableViewField summary = new TableViewField("Summary",
			"summary", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField logUser = new TableViewField("User",
			"logUserFullName", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField logValue = new TableViewField("Hours",
			"logvalue", UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField billable = new TableViewField("Billable",
			"isbillable", UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField logForDate = new TableViewField("Logged Date",
			"logforday", UIConstants.TABLE_DATE_TIME_WIDTH);

	public static TableViewField project = new TableViewField("Project",
			"projectName", UIConstants.TABLE_X_LABEL_WIDTH);
}
