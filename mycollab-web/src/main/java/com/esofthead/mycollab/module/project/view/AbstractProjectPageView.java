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
package com.esofthead.mycollab.module.project.view;

import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class AbstractProjectPageView extends AbstractPageView {

	private static final long serialVersionUID = 1L;
	private Label headerText;
	private CssLayout contentWrapper;
	private MHorizontalLayout header;
	private Image titleIcon;

	public AbstractProjectPageView(String headerText, String iconName) {
		super();

		this.titleIcon = new Image(null,
				MyCollabResource.newResource("icons/24/project/" + iconName));
		this.headerText = new Label(headerText);
		super.addComponent(constructHeader());

		contentWrapper = new CssLayout();
		contentWrapper.setStyleName("content-wrapper");
		super.addComponent(contentWrapper);

	}

	private ComponentContainer constructHeader() {
		header = new MHorizontalLayout();
		this.headerText.setStyleName("hdr-text");

		header.with(titleIcon, headerText)
				.withAlign(titleIcon, Alignment.MIDDLE_LEFT)
				.withAlign(titleIcon, Alignment.MIDDLE_LEFT).expand(headerText)
				.withStyleName("hdr-view").withWidth("100%").withSpacing(true)
				.withMargin(true);

		return header;
	}

	public void addHeaderRightContent(Component c) {
		header.addComponent(c);
	}

	@Override
	public void addComponent(Component c) {
		contentWrapper.addComponent(c);
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		contentWrapper.replaceComponent(oldComponent, newComponent);
	}

	public ComponentContainer getBody() {
		return contentWrapper;
	}
}
