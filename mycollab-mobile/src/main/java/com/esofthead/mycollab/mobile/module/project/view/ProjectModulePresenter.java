package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectGenericPresenter;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class ProjectModulePresenter extends
		ProjectGenericPresenter<ProjectModule> {

	private static final long serialVersionUID = 6940806138148601147L;

	public ProjectModulePresenter() {
		super(ProjectModule.class);
	}

	@Override
	protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
		EventBusFactory.getInstance().post(
				new ProjectEvent.GotoProjectList(this, null));
	}

}
