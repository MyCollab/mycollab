package com.esofthead.mycollab.module.project.view.parameters;

import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TaskFilterParameter {

	private TaskSearchCriteria searchCriteria;
	
	private String screenTitle;
	
	public TaskFilterParameter(TaskSearchCriteria searchCriteria, String screenTitle) {
		this.searchCriteria = searchCriteria;
		this.screenTitle = screenTitle;
	}

	public TaskSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(TaskSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}
}
