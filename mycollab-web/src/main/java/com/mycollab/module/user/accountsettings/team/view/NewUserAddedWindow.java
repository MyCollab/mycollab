/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.event.UserEvent;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
class NewUserAddedWindow extends MWindow {
    NewUserAddedWindow(final SimpleUser user, String uncryptPassword) {
        super(UserUIContext.getMessage(UserI18nEnum.NEW));
        MVerticalLayout content = new MVerticalLayout();
        this.withModal(true).withResizable(false).withClosable(false).withCenter().withWidth("600px").withContent(content);

        ELabel infoLbl = ELabel.html(VaadinIcons.CHECK_CIRCLE.getHtml() + UserUIContext.getMessage(UserI18nEnum.OPT_NEW_USER_CREATED,
                user.getDisplayName()));
        content.with(infoLbl);

        String signinInstruction = UserUIContext.getMessage(UserI18nEnum.OPT_SIGN_IN_MSG, AppUI.getSiteUrl(), AppUI.getSiteUrl());
        content.with(new MVerticalLayout(ELabel.html(signinInstruction),
                new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL)).withStyleName(WebThemes.META_INFO),
                new Label("    " + user.getUsername()),
                new ELabel(UserUIContext.getMessage(ShellI18nEnum.FORM_PASSWORD)).withStyleName(WebThemes.META_INFO),
                new Label("    " + (uncryptPassword != null ? uncryptPassword : UserUIContext.getMessage(UserI18nEnum.OPT_USER_SET_OWN_PASSWORD)))));

        content.with(new ELabel(UserUIContext.getMessage(GenericI18Enum.HELP_SPAM_FILTER_PREVENT_MESSAGE)).withStyleName
                (WebThemes.META_INFO).withFullWidth());

        MButton createMoreUserBtn = new MButton(UserUIContext.getMessage(UserI18nEnum.OPT_CREATE_ANOTHER_USER), clickEvent -> {
            EventBusFactory.getInstance().post(new UserEvent.GotoAdd(this, null));
            close();
        }).withStyleName(WebThemes.BUTTON_LINK);

        MButton doneBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_DONE), clickEvent -> {
            EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
            close();
        }).withStyleName(WebThemes.BUTTON_ACTION);

        MHorizontalLayout buttonControls = new MHorizontalLayout(createMoreUserBtn, doneBtn).withFullWidth()
                .withAlign(createMoreUserBtn, Alignment.MIDDLE_LEFT).withAlign(doneBtn, Alignment.MIDDLE_RIGHT);
        content.with(buttonControls);
    }
}
