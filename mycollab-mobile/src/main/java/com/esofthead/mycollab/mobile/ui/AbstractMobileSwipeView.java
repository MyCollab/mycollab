package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationView;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class AbstractMobileSwipeView extends MobileNavigationView implements
		PageView {

	private static final long serialVersionUID = -5179416042698544018L;

	public static String SAVE_ACTION = AppContext
			.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL);
	public static String SAVE_AND_NEW_ACTION = "Save & New";
	public static String EDIT_ACTION = "Edit";
	public static String CANCEL_ACTION = AppContext
			.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL);
	public static String DELETE_ACTION = "Delete";
	public static String CLONE_ACTION = "Clone";

	protected ViewState viewState;

	public AbstractMobileSwipeView() {
		super();
		this.setStyleName("mobilenavview");
        this.setToggleButton(true);
	}

	public ViewState getViewState() {
		return viewState;
	}

	public void setViewState(ViewState viewState) {
		this.viewState = viewState;
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@Override
	public void addViewListener(ViewListener listener) {
		addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent.class, listener,
				ViewListener.viewInitMethod);
	}

	@Override
	public NavigationManager getNavigationManager() {
		Component parent = this.getParent();
		while (parent != null) {
			if (parent instanceof NavigationManager)
				return (NavigationManager) parent;
			else
				parent = parent.getParent();
		}
		return null;
	}

}
