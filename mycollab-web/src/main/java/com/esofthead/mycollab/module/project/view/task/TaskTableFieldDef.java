package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TaskTableFieldDef {
	public static TableViewField id = new TableViewField("", "id",
			UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField taskname = new TableViewField(
			LocalizationHelper.getMessage(TaskI18nEnum.TABLE_TASK_NAME_HEADER),
			"taskname", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField startdate = new TableViewField(
			LocalizationHelper.getMessage(TaskI18nEnum.TABLE_START_DATE_HEADER),
			"startdate", UIConstants.TABLE_DATE_WIDTH);

	public static TableViewField duedate = new TableViewField(
			LocalizationHelper.getMessage(TaskI18nEnum.TABLE_DUE_DATE_HEADER),
			"deadline", UIConstants.TABLE_DATE_WIDTH);

	public static TableViewField percentagecomplete = new TableViewField(
			LocalizationHelper
					.getMessage(TaskI18nEnum.TABLE_PER_COMPLETE_HEADER),
			"percentagecomplete", UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField assignee = new TableViewField("Assignee",
			"assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH);
}
