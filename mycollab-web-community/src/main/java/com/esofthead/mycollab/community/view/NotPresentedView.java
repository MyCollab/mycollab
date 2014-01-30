/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.community.view;

import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.web.MyCollabResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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
				.newResource("icons/not_present.png"));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_CENTER);

		Label label = new Label("Module is not presented for community edition");
		label.setStyleName("h2_community");
		layout.addComponent(label);
		layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

		layoutWapper.addComponent(layout);
		layoutWapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
		this.addComponent(layoutWapper);
		this.setComponentAlignment(layoutWapper, Alignment.MIDDLE_CENTER);
	}
}
