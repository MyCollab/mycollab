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
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.events.UserEvent;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.B;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.7
 */
class GetStartedInstructionWindow extends Window {
    private MVerticalLayout contentLayout;

    public GetStartedInstructionWindow(SimpleUser user) {
        super("Getting started instructions");
        this.setResizable(false);
        this.setModal(true);
        this.setWidth("600px");
        contentLayout = new MVerticalLayout();
        this.setContent(contentLayout);
        center();
        displayInfo(user);
    }

    private void displayInfo(SimpleUser user) {
        Div infoDiv = new Div().appendText("You have not setup SMTP account properly. So we can not send the invitation by email automatically. Please copy/paste below paragraph and inform to the user by yourself").setStyle("font-weight:bold;color:red");
        Label infoLbl = new Label(infoDiv.write(), ContentMode.HTML);

        Div userInfoDiv = new Div().appendText("Your username is ").appendChild(new B().appendText(user.getEmail()));
        Label userInfoLbl = new Label(userInfoDiv.write(), ContentMode.HTML);

        if (Boolean.TRUE.equals(user.getIsAccountOwner())) {
            user.setRoleName(AppContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER));
        }
        Div roleInfoDiv = new Div().appendText("Your role is ").appendChild(new B().appendText(user.getRoleName()));
        Label roleInfoLbl = new Label(roleInfoDiv.write(), ContentMode.HTML);
        contentLayout.with(infoLbl, userInfoLbl, roleInfoLbl);

        final MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
        final Button addNewBtn = new Button("Create another user", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(
                    final Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new UserEvent.GotoAdd(GetStartedInstructionWindow.this, null));
                close();
            }
        });
        addNewBtn.setStyleName(UIConstants.BUTTON_ACTION);
        Button doneBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_DONE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                close();
            }
        });
        doneBtn.setStyleName(UIConstants.BUTTON_ACTION);
        controlsBtn.with(addNewBtn, doneBtn);
        contentLayout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
    }
}
