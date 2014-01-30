/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class AddViewLayout extends CustomLayoutExt {

	private static final long serialVersionUID = 1L;

	private final Label titleLbl;
	private final Embedded icon;
	private final HorizontalLayout header;

	public AddViewLayout(final String title, final Resource resource) {
		super("addView");

		this.header = new HorizontalLayout();
		this.header.setWidth("100%");
		this.header.setSpacing(true);
		this.header.setHeight("30px");

		this.icon = new Embedded();
		this.setTitleIcon(resource);
		this.header.addComponent(this.icon);
		this.titleLbl = new Label();
		this.titleLbl.setStyleName("headerName");
		this.titleLbl.setImmediate(true);

		this.header.addComponent(this.titleLbl);
		this.header.setExpandRatio(titleLbl, 1.0f);

		if (title == null) {
			if (resource != null) {
				this.setTitle("Undefined");
			}
		} else {
			this.setTitle(title);
		}

		this.addComponent(this.header, "addViewHeader");
	}

	public void addBody(final ComponentContainer body) {
		this.addComponent(body, "addViewBody");
	}

	public void addBottomControls(final ComponentContainer bottomControls) {
		this.addComponent(bottomControls, "addViewBottomControls");
	}

	public void addHeaderRight(final ComponentContainer headerRight) {
		this.header.addComponent(headerRight);
	}

	public void addTitleStyleName(final String styleName) {
		this.titleLbl.addStyleName(styleName);
	}

	public void setTitleStyleName(final String styleName) {
		this.titleLbl.setStyleName(styleName);
	}

	public void removeTitleStyleName(final String styleName) {
		this.titleLbl.removeStyleName(styleName);
	}

	public void addTopControls(final ComponentContainer topControls) {
		this.addComponent(topControls, "addViewTopControls");
	}

	public void setTitle(final String title) {
		this.titleLbl.setValue(title);
	}

	public void setTitleIcon(final Resource resource) {
		if (resource != null) {
			this.icon.setSource(resource);
		}
	}

}
