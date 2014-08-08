/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.ui;

import java.io.Serializable;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.mvp.PageView.ViewEvent;
import com.esofthead.mycollab.vaadin.mvp.PageView.ViewListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class AbstractMobileMainView extends VerticalLayout implements
		PageView, Serializable {
	private static final long serialVersionUID = 1L;

	public static String SAVE_ACTION = AppContext
			.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL);
	public static String SAVE_AND_NEW_ACTION = "Save & New";
	public static String EDIT_ACTION = "Edit";
	public static String CANCEL_ACTION = AppContext
			.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL);
	public static String DELETE_ACTION = "Delete";
	public static String CLONE_ACTION = "Clone";
	protected ViewState viewState;

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
}