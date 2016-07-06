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
package com.mycollab.mobile.module.project.ui;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.*;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ProjectMobileMenuPageView extends AbstractMobileMenuPageView {
    @Override
    protected void buildNavigateMenu() {
        getMenu().setWidth("80%");
        addSection("Views:");

        Button prjButton = new Button("Projects", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
            }
        });
        prjButton.setIcon(FontAwesome.BUILDING);
        addMenuItem(prjButton);

        // Buttons with styling (slightly smaller with left-aligned text)
        Button activityBtn = new Button("Activities", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new ProjectEvent.MyProjectActivities(this, CurrentProjectVariables.getProjectId()));
            }
        });
        activityBtn.setIcon(FontAwesome.INBOX);
        addMenuItem(activityBtn);

        // add more buttons for a more realistic look.
        Button messageBtn = new Button("Messages", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
            }
        });
        messageBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE));
        addMenuItem(messageBtn);

        Button phaseBtn = new Button("Phases", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
            }
        });
        phaseBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
        addMenuItem(phaseBtn);

        Button taskBtn = new Button("Tasks", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new TaskEvent.GotoList(this, null));
            }
        });
        taskBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));
        addMenuItem(taskBtn);

        Button bugBtn = new Button("Bugs", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null));
            }
        });
        bugBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
        addMenuItem(bugBtn);

        Button userBtn = new Button("Users", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
            }
        });
        userBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER));
        addMenuItem(userBtn);

        addSection("Settings:");

        Button logoutBtn = new Button("Logout", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new ShellEvent.LogOut(this));
            }
        });
        logoutBtn.setIcon(FontAwesome.SIGN_OUT);
        addMenuItem(logoutBtn);
    }
}
