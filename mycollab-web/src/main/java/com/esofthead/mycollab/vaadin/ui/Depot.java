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

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class Depot extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private boolean isOpenned = true;
	protected HorizontalLayout header;
	protected final Label headerLbl;
	protected AbstractOrderedLayout headerContent;
	protected ComponentContainer bodyContent;

	public Depot(final String title, final ComponentContainer component) {
		this(title, null, component);
	}

	public Depot(final String title, final AbstractOrderedLayout headerElement,
			final ComponentContainer component) {
		this(new Label(title), headerElement, component, "100%", "100%");
	}

	public Depot(final String title, final AbstractOrderedLayout headerElement,
			final ComponentContainer component, final String headerWidth,
			final String headerLeftWidth) {
		this(new Label(title), headerElement, component, headerWidth,
				headerLeftWidth);
	}

	public Depot(final Label titleLbl,
			final AbstractOrderedLayout headerElement,
			final ComponentContainer component, final String headerWidth,
			final String headerLeftWidth) {
		this.setStyleName("depotComp");
		this.setMargin(new MarginInfo(true, false, false, false));
		this.header = new HorizontalLayout();
		this.header.setStyleName("depotHeader");
		this.header.setWidth(headerWidth);
		this.bodyContent = component;
		if (headerElement != null) {
			this.headerContent = headerElement;
		} else {
			this.headerContent = new HorizontalLayout();
			((HorizontalLayout) this.headerContent).setSpacing(true);
			((HorizontalLayout) this.headerContent).setMargin(true);
			((HorizontalLayout) this.headerContent)
					.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
			this.headerContent.setVisible(false);
		}

		this.headerContent.setStyleName("header-elements");
		this.headerContent.setWidth(Sizeable.SIZE_UNDEFINED,
				Sizeable.Unit.PIXELS);
		// this.headerContent.setHeight("100%");
		this.headerContent.setSizeUndefined();

		this.addComponent(this.header);

		final VerticalLayout headerLeft = new VerticalLayout();
		this.headerLbl = titleLbl;
		this.headerLbl.setStyleName("h2");
		this.headerLbl.setWidth("100%");
		headerLeft.addComponent(this.headerLbl);
		headerLeft.setStyleName("depot-title");
		headerLeft.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void layoutClick(final LayoutClickEvent event) {
				Depot.this.isOpenned = !Depot.this.isOpenned;
				if (Depot.this.isOpenned) {
					Depot.this.bodyContent.setVisible(true);
					Depot.this.removeStyleName("collapsed");
				} else {
					Depot.this.bodyContent.setVisible(false);
					Depot.this.addStyleName("collapsed");
				}
			}
		});
		final CssLayout headerWrapper = new CssLayout();
		headerWrapper.addComponent(headerLeft);
		headerWrapper.setStyleName("header-wrapper");
		headerWrapper.setWidth(headerLeftWidth);
		this.header.addComponent(headerWrapper);
		this.header.setComponentAlignment(headerWrapper, Alignment.MIDDLE_LEFT);
		this.header.addComponent(this.headerContent);
		this.header.setComponentAlignment(this.headerContent,
				Alignment.TOP_RIGHT);
		this.header.setExpandRatio(headerWrapper, 1.0f);

		final CustomComponent customComp = new CustomComponent(this.bodyContent);
		customComp.setWidth("100%");
		this.bodyContent.addStyleName("depotContent");

		this.addComponent(customComp);
		this.setComponentAlignment(customComp, Alignment.TOP_CENTER);
	}

	public Depot(final String title, final ComponentContainer component,
			final String headerWidth) {
		this(new Label(title), null, component, headerWidth, "250px");
	}

	public void addHeaderElement(final Component component) {
		if (component != null) {
			this.headerContent.addComponent(component);
			this.headerContent.setComponentAlignment(component,
					Alignment.MIDDLE_RIGHT);
			this.headerContent.setVisible(true);
		}
	}

	public Label getHeaderLbl() {
		return this.headerLbl;
	}

	public void setContentBorder(final boolean hasBorder) {
		if (hasBorder) {
			this.bodyContent.addStyleName("bordered");
		} else {
			this.bodyContent.removeStyleName("bordered");
		}
	}

	public void setTitle(final String title) {
		this.headerLbl.setValue(title);
	}

	public void setHeaderColor(final boolean hasColor) {
		if (hasColor) {
			this.headerContent.addStyleName("colored-header");
		} else {
			this.headerContent.removeStyleName("colored-header");
		}
	}
}
