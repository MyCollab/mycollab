package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.google.common.eventbus.EventBus;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class ProjectModuleController implements IController {

	private static final long serialVersionUID = 8999456416358169209L;

	private final MobileNavigationManager navManager;

	private final EventBus eventBus;

	public ProjectModuleController(MobileNavigationManager navigationManager) {
		this.navManager = navigationManager;
		this.eventBus = EventBusFactory.getInstance();

//		bindProjectEvents();
	}

//	private void bindProjectEvents() {
//		eventBus.register(new ApplicationEventListener<ProjectEvent.GotoDashboard>() {
//
//			private static final long serialVersionUID = -9006615798118115613L;
//
//			@Subscribe
//			@Override
//			public void handle(ProjectEvent.GotoDashboard event) {
//
//			}
//		});
//	}

}
