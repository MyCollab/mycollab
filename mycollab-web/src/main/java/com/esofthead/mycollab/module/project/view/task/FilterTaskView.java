package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.vaadin.mvp.PageView;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public interface FilterTaskView extends PageView {

	void filterTasks(TaskFilterParameter filterParam);
}
