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

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractCssPageView extends CssLayout implements PageView, Serializable {
	protected CssLayout vTabsheetFix;
	private String vTabsheetFixWidth = "250px";
	private boolean vTabsheetIsLeft = false;

	public AbstractCssPageView() {
		this(false);
	}

    public AbstractCssPageView(boolean isLeftAlign) {
        this.setStyleName("page-view");
        this.setWidth("100%");
        this.vTabsheetIsLeft = isLeftAlign;
        this.updateVerticalTabsheetFixStatus();
    }

	public void updateVerticalTabsheetFixStatus() {
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
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addViewListener(ViewListener listener) {
		addListener(ViewEvent.VIEW_IDENTIFIER(), ViewEvent.class, listener,
				ViewListener.viewInitMethod);
	}
}
