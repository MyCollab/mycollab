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
package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.MessageEvent;
import com.esofthead.mycollab.mobile.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.vaadin.thomas.slidemenu.SlideMenu;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ProjectMobileMenuPageView extends AbstractMobileMenuPageView {
    @Override
    protected void buildNavigateMenu() {

        // Section labels have a bolded style
        Label l = new Label("Views:");
        l.addStyleName(SlideMenu.STYLENAME_SECTIONLABEL);
        getMenu().addComponent(l);

        Button prjButton = new Button("Projects", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getMenu().close();
                EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
            }
        });
        prjButton.setIcon(FontAwesome.BUILDING);
        prjButton.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(prjButton);

        // Buttons with styling (slightly smaller with left-aligned text)
        Button activityBtn = new Button("Activities", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getMenu().close();
                EventBusFactory.getInstance().post(new ProjectEvent.MyProjectActivities(this, null));
            }
        });
        activityBtn.setIcon(FontAwesome.INBOX);
        activityBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(activityBtn);

        // add more buttons for a more realistic look.
        Button messageBtn = new Button("Messages", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getMenu().close();
                EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
            }
        });
        messageBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE));
        messageBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(messageBtn);

        Button phaseBtn = new Button("Phases", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getMenu().close();
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
            }
        });
        phaseBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        phaseBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
        getMenu().addComponent(phaseBtn);

        Button ticketBtn = new Button("Tickets");
        ticketBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        ticketBtn.setIcon(FontAwesome.TICKET);
        getMenu().addComponent(ticketBtn);

        Button userBtn = new Button("Users", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
            }
        });
        userBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER));
        userBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(userBtn);

        l = new Label("Settings:");
        l.addStyleName(SlideMenu.STYLENAME_SECTIONLABEL);
        getMenu().addComponent(l);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setIcon(FontAwesome.SIGN_OUT);
        logoutBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(logoutBtn);
    }
}
