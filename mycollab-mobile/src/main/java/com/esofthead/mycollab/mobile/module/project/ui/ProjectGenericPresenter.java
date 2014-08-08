package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class ProjectGenericPresenter<V extends PageView> extends
		AbstractPresenter<V> {

	private static final long serialVersionUID = 2162143696476839340L;

	public ProjectGenericPresenter(Class<V> viewClass) {
		super(viewClass);
	}

	@Override
	protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
		if (!view.isAttached()) {
			if (navigator instanceof NavigationManager)
				((NavigationManager) navigator).navigateTo(view.getWidget());
			else {
				navigator.removeAllComponents();
				navigator.addComponent(view.getWidget());
			}
		}
	}

}
