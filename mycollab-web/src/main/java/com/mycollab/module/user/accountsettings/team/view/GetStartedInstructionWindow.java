/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.hp.gagawa.java.elements.B;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.event.UserEvent;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.7
 */
class GetStartedInstructionWindow extends MWindow {
    private MVerticalLayout contentLayout;

    public GetStartedInstructionWindow(SimpleUser user) {
        super("Getting started instructions");
        contentLayout = new MVerticalLayout();
        this.withResizable(false).withModal(true).withWidth("600px").withContent(contentLayout).withCenter();
        displayInfo(user);
    }

    private void displayInfo(SimpleUser user) {
        Div infoDiv = new Div().appendText("You have not setup SMTP account properly. So we can not send the invitation by email automatically. Please copy/paste below paragraph and inform to the user by yourself").setStyle("font-weight:bold;color:red");
        Label infoLbl = new Label(infoDiv.write(), ContentMode.HTML);

        Div userInfoDiv = new Div().appendText("Your username is ").appendChild(new B().appendText(user.getEmail()));
        Label userInfoLbl = ELabel.html(userInfoDiv.write());

        if (Boolean.TRUE.equals(user.isAccountOwner())) {
            user.setRoleName(UserUIContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER));
        }
        Div roleInfoDiv = new Div().appendText("Your role is ").appendChild(new B().appendText(user.getRoleName()));
        Label roleInfoLbl = new Label(roleInfoDiv.write(), ContentMode.HTML);
        contentLayout.with(infoLbl, userInfoLbl, roleInfoLbl);

        final Button addNewBtn = new Button("Create another user", clickEvent -> {
            EventBusFactory.getInstance().post(new UserEvent.GotoAdd(GetStartedInstructionWindow.this, null));
            close();
        });
        addNewBtn.setStyleName(WebThemes.BUTTON_ACTION);

        Button doneBtn = new Button(UserUIContext.getMessage(GenericI18Enum.ACTION_DONE), clickEvent -> close());
        doneBtn.setStyleName(WebThemes.BUTTON_ACTION);

        final MHorizontalLayout controlsBtn = new MHorizontalLayout(addNewBtn, doneBtn).withMargin(true);
        contentLayout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
    }
}
