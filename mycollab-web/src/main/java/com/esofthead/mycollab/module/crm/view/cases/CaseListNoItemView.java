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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.localization.CaseI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
@ViewComponent
public class CaseListNoItemView extends AbstractPageView {
	private static final long serialVersionUID = 1L;

	public CaseListNoItemView() {

		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("case-noitem");
		layout.setWidth("800px");
		layout.setSpacing(true);
		layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		layout.setMargin(true);

		Image image = new Image(null,
				MyCollabResource.newResource("icons/48/crm/case.png"));
		layout.addComponent(image);

		Label title = new Label(
				AppContext.getMessage(CaseI18nEnum.NO_ITEM_VIEW_TITLE));
		title.addStyleName("h2");
		title.setWidth(SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
		layout.addComponent(title);

		Label contact = new Label(
				AppContext.getMessage(CaseI18nEnum.NO_ITEM_VIEW_HINT));
		contact.setWidth(SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
		layout.addComponent(contact);

		Button btCreateContact = new Button("Create Case",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new CaseEvent.GotoAdd(this, null));
					}
				});

		HorizontalLayout links = new HorizontalLayout();

		links.addComponent(btCreateContact);
		btCreateContact.addStyleName(UIConstants.THEME_GREEN_LINK);

		/*
		 * Label or = new Label("Or"); or.setStyleName("h2");
		 * links.addComponent(or);
		 * 
		 * Button btImportContact = new Button("Import Cases", new
		 * Button.ClickListener() { private static final long serialVersionUID =
		 * 1L;
		 * 
		 * @Override public void buttonClick(ClickEvent arg0) {
		 * UI.getCurrent().addWindow(new CaseImportWindow()); } });
		 * 
		 * btImportContact.addStyleName(UIConstants.THEME_GRAY_LINK);
		 * 
		 * 
		 * links.addComponent(btImportContact);
		 */
		links.setSpacing(true);

		layout.addComponent(links);
		this.addComponent(layout);
		this.setComponentAlignment(layout, Alignment.TOP_CENTER);
	}
}
