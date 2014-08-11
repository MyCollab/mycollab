/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.shell.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobileMainView;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class MainView extends AbstractMobileMainView {
	private static final long serialVersionUID = 1316340508967377888L;

	public MainView() {
		super();

		initUI();
	}

	private void initUI() {
		this.setStyleName("main-view");
		this.setSizeFull();

		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("content-wrapper");
		contentLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		contentLayout.setWidth("320px");

		Image mainLogo = new Image(null, new ThemeResource("icons/logo_m.png"));
		contentLayout.addComponent(mainLogo);

		Label introText = new Label(
				"MyCollab helps you do all your office jobs on the computers, phones and tablets you use");
		introText.setStyleName("intro-text");
		contentLayout.addComponent(introText);

		CssLayout welcomeTextWrapper = new CssLayout();
		welcomeTextWrapper.setStyleName("welcometext-wrapper");
		welcomeTextWrapper.setWidth("100%");
		welcomeTextWrapper.setHeight("15px");
		contentLayout.addComponent(welcomeTextWrapper);

		ModuleButton crmButton = new ModuleButton(
				AppContext.getMessage(GenericI18Enum.MODULE_CRM));
		crmButton.setWidth("100%");
		crmButton.addStyleName("crm");
		crmButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -1218427186205574547L;

			@Override
			public void buttonClick(ClickEvent event) {
				EventBusFactory.getInstance().post(
						new ShellEvent.GotoCrmModule(this, null));
			}
		});

		contentLayout.addComponent(crmButton);

		ModuleButton pmButton = new ModuleButton(
				AppContext.getMessage(GenericI18Enum.MODULE_PROJECT));
		pmButton.setWidth("100%");
		pmButton.addStyleName("project");
		pmButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -5323408319082242586L;

			@Override
			public void buttonClick(ClickEvent event) {
				EventBusFactory.getInstance().post(
						new ShellEvent.GotoProjectModule(this, null));
			}
		});
		contentLayout.addComponent(pmButton);

		ModuleButton fileButton = new ModuleButton(
				AppContext.getMessage(GenericI18Enum.MODULE_DOCUMENT));
		fileButton.setWidth("100%");
		fileButton.addStyleName("document");
		contentLayout.addComponent(fileButton);

		this.addComponent(contentLayout);
	}
}
