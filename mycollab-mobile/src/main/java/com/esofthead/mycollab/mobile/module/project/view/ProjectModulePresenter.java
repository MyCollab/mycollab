package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.ui.ProjectGenericPresenter;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectNavigationMenu;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
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
		super.onGo(navigator, data);
		((MobileNavigationManager) navigator)
				.setNavigationMenu(new ProjectNavigationMenu());
		((MobileNavigationManager) navigator).navigateTo(view);
	}

}
