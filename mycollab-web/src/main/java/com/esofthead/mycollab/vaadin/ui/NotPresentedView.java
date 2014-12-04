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

import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class NotPresentedView extends AbstractPageView {
	private static final long serialVersionUID = 1L;

	public NotPresentedView() {
		this.setHeight("370px");
		this.setWidth("100%");
		VerticalLayout layoutWapper = new VerticalLayout();
		layoutWapper.setWidth("100%");

		VerticalLayout layout = new VerticalLayout();
		final Embedded titleIcon = new Embedded();
		titleIcon.setSource(MyCollabResource
				.newResource(WebResourceIds._not_present));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_CENTER);

		Label label = new Label("The feature is not presented for this edition");
		label.setStyleName("h2_community");
		layout.addComponent(label);
		layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

		layoutWapper.addComponent(layout);
		layoutWapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
		this.addComponent(layoutWapper);
		this.setComponentAlignment(layoutWapper, Alignment.MIDDLE_CENTER);
	}
}
