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
package com.esofthead.mycollab.vaadin.mvp;

import java.io.Serializable;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractCssPageView extends CssLayout implements
		PageView, Serializable {

	private CssLayout vTabsheetFix;
	private String vTabsheetFixWidth = "250px";
	private boolean vTabsheetIsLeft = true;
	private boolean showTabsheetFix = false;

	public AbstractCssPageView() {
		this.setStyleName("page-view");
	}

	private void updateVerticalTabsheetFixStatus() {
		if (showTabsheetFix) {
			if (vTabsheetFix == null) {
				vTabsheetFix = new CssLayout();
				vTabsheetFix.setStyleName("verticaltabsheet-fix");
				this.addComponentAsFirst(vTabsheetFix);
			} else if (vTabsheetFix.getParent() != this) {
				this.addComponentAsFirst(vTabsheetFix);
			}
			vTabsheetFix.setWidth(this.vTabsheetFixWidth);
			if (this.vTabsheetIsLeft)
				vTabsheetFix.addStyleName("is-left");
			else
				vTabsheetFix.removeStyleName("is-left");
		} else {
			if (vTabsheetFix != null && vTabsheetFix.getParent() == this) {
				this.removeComponent(vTabsheetFix);
			}
		}
	}

	public void setVerticalTabsheetFix(boolean value) {
		this.showTabsheetFix = value;
		updateVerticalTabsheetFixStatus();
	}

	public void setVerticalTabsheetFixToLeft(boolean isLeft) {
		this.vTabsheetIsLeft = isLeft;
		updateVerticalTabsheetFixStatus();
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addViewListener(ViewListener listener) {
		addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent.class, listener,
				ViewListener.viewInitMethod);
	}
}
