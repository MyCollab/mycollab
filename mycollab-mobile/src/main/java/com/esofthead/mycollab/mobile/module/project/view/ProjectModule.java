package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.ui.AbstractMobileSwipeView;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.IModule;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */

@ViewComponent
public class ProjectModule extends AbstractMobileSwipeView implements IModule {

	private static final long serialVersionUID = -537762284500231520L;

	public ProjectModule() {
		ControllerRegistry.addController(new ProjectModuleController(
				(MobileNavigationManager) UI.getCurrent().getContent()));

		Label introLbl = new Label("This is project dashboard");
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		this.setContent(mainLayout);
		mainLayout.addComponent(introLbl);
		this.setToggleButton(true);
		this.setCaption("Dashboard");
	}

}
