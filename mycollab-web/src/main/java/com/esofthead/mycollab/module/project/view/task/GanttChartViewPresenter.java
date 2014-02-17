package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class GanttChartViewPresenter extends AbstractPresenter<GanttChartView> {
	private static final long serialVersionUID = 1L;

	public GanttChartViewPresenter() {
		super(GanttChartView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS)) {
			TaskContainer taskContainer = (TaskContainer) container;
			taskContainer.removeAllComponents();
			taskContainer.addComponent(view.getWidget());
			view.displayGanttChart();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}

	}

}
