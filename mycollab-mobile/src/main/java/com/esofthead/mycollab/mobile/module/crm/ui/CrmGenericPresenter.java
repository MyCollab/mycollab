package com.esofthead.mycollab.mobile.module.crm.ui;

import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;

public class CrmGenericPresenter<V extends PageView> extends
		AbstractPresenter<V> {

	private static final long serialVersionUID = 1L;

	public CrmGenericPresenter(Class<V> viewClass) {
		super(viewClass);
	}

	@Override
	protected void onGo(MobileNavigationManager navigator, ScreenData<?> data) {
		navigator.navigateTo(view.getWidget());
	}
}
