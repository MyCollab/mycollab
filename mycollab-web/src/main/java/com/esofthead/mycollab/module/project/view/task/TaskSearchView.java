package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable;

public interface TaskSearchView extends PageView {
	public static final String VIEW_DEF_ID = "project-task-list";

	void setTitle(String title);

	HasSearchHandlers<TaskSearchCriteria> getSearchHandlers();

	IPagedBeanTable<TaskSearchCriteria, SimpleTask> getPagedBeanTable();

	void moveToAdvanceSearch();
	void moveToBasicSearch();

	void setSearchInputValue(String value);
}
