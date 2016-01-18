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
package com.esofthead.mycollab.mobile.shell.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobileMainView;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class MainView extends AbstractMobileMainView {
    private static final long serialVersionUID = 1316340508967377888L;

    public MainView() {
        super();
        this.setSizeFull();

        MVerticalLayout contentLayout = new MVerticalLayout().withStyleName("content-wrapper").withFullWidth();
        contentLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Image mainLogo = new Image(null, new ThemeResource("icons/logo_m.png"));
        contentLayout.addComponent(mainLogo);

        Label introText = new Label("MyCollab helps you do all your office jobs on the computers, phones and tablets you use");
        introText.setStyleName("intro-text");
        contentLayout.addComponent(introText);

        CssLayout welcomeTextWrapper = new CssLayout();
        welcomeTextWrapper.setStyleName("welcometext-wrapper");
        welcomeTextWrapper.setWidth("100%");
        welcomeTextWrapper.setHeight("15px");
        contentLayout.addComponent(welcomeTextWrapper);

        Button crmButton = new Button(AppContext.getMessage(GenericI18Enum.MODULE_CRM), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
            }
        });
        crmButton.addStyleName(UIConstants.BUTTON_ACTION);
        crmButton.setWidth("100%");

        contentLayout.addComponent(crmButton);

        Button pmButton = new Button(AppContext.getMessage(GenericI18Enum.MODULE_PROJECT), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
            }
        });
        pmButton.setWidth("100%");
        pmButton.addStyleName(UIConstants.BUTTON_ACTION);
        contentLayout.addComponent(pmButton);

        this.addComponent(contentLayout);
    }

    public void display() {
    }
}
