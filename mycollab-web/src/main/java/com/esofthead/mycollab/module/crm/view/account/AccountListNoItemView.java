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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
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
public class AccountListNoItemView extends AbstractPageView {
	private static final long serialVersionUID = 1L;

	public AccountListNoItemView() {

		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("case-noitem");
		layout.setWidth("800px");
		layout.setSpacing(true);
		layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		layout.setMargin(true);

		Image image = new Image(null,
				MyCollabResource.newResource("icons/48/crm/account.png"));
		layout.addComponent(image);

		Label title = new Label(
				AppContext.getMessage(AccountI18nEnum.VIEW_NO_ITEM_TITLE));
		title.addStyleName("h2");
		title.setWidthUndefined();
		layout.addComponent(title);

		Label body = new Label(
				AppContext.getMessage(AccountI18nEnum.VIEW_NO_ITEM_HINT));
		body.setWidthUndefined();
		layout.addComponent(body);

		Button createAccountBtn = new Button(
				AppContext.getMessage(AccountI18nEnum.BUTTON_NEW_ACCOUNT),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBusFactory.getInstance().post(
								new AccountEvent.GotoAdd(this, null));
					}
				});

		HorizontalLayout links = new HorizontalLayout();

		links.addComponent(createAccountBtn);
		createAccountBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

		/*
		 * Label or = new Label("Or"); or.setStyleName("h2");
		 * links.addComponent(or);
		 * 
		 * Button btImportContact = new Button("Import Accounts", new
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
