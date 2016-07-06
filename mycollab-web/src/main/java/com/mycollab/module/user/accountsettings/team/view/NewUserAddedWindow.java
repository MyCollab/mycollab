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
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.events.UserEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class NewUserAddedWindow extends Window {
    public NewUserAddedWindow(final SimpleUser user, String uncryptPassword) {
        super(AppContext.getMessage(UserI18nEnum.NEW));
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(false);
        this.center();
        this.setWidth("600px");
        MVerticalLayout content = new MVerticalLayout();
        this.setContent(content);

        ELabel infoLbl = new ELabel(FontAwesome.CHECK_CIRCLE.getHtml() + AppContext.getMessage(UserI18nEnum.OPT_NEW_USER_CREATED,
                user.getDisplayName()), ContentMode.HTML);
        content.with(infoLbl);

        String signinInstruction = AppContext.getMessage(UserI18nEnum.OPT_SIGN_IN_MSG, AppContext.getSiteUrl(), AppContext.getSiteUrl());
        content.with(new MVerticalLayout(new Label(signinInstruction, ContentMode.HTML),
                new ELabel(AppContext.getMessage(ShellI18nEnum.FORM_EMAIL)).withStyleName(UIConstants.META_INFO),
                new Label("    " + user.getUsername()),
                new ELabel(AppContext.getMessage(ShellI18nEnum.FORM_PASSWORD)).withStyleName(UIConstants.META_INFO),
                new Label("    " + (uncryptPassword != null ? uncryptPassword : AppContext.getMessage(UserI18nEnum.OPT_USER_SET_OWN_PASSWORD)))));

        Button sendEmailBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_SEND_EMAIL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ExtMailService mailService = AppContextUtil.getSpringBean(ExtMailService.class);
                if (!mailService.isMailSetupValid()) {
                    UI.getCurrent().addWindow(new GetStartedInstructionWindow(user));
                } else {

                    NotificationUtil.showNotification(AppContext.getMessage(GenericI18Enum.HELP_SPAM_FILTER_PREVENT_TITLE),
                            AppContext.getMessage(GenericI18Enum.HELP_SPAM_FILTER_PREVENT_MESSAGE));
                }
            }
        });

        content.with(new ELabel(AppContext.getMessage(GenericI18Enum.HELP_SPAM_FILTER_PREVENT_MESSAGE)).withStyleName
                (UIConstants.META_INFO));

        Button createMoreUserBtn = new Button(AppContext.getMessage(UserI18nEnum.OPT_CREATE_ANOTHER_USER), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new UserEvent.GotoAdd(this, null));
                close();
            }
        });
        createMoreUserBtn.addStyleName(UIConstants.BUTTON_LINK);

        Button doneBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_DONE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
                close();
            }
        });
        doneBtn.addStyleName(UIConstants.BUTTON_ACTION);
        MHorizontalLayout buttonControls = new MHorizontalLayout(createMoreUserBtn, doneBtn).withFullWidth()
                .withAlign(createMoreUserBtn, Alignment.MIDDLE_LEFT).withAlign(doneBtn, Alignment.MIDDLE_RIGHT);
        content.with(buttonControls);
    }
}
