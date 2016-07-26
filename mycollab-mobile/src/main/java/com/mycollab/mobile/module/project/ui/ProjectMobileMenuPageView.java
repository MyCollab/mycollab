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
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ProjectMobileMenuPageView extends AbstractMobileMenuPageView {
    @Override
    protected void buildNavigateMenu() {
        getMenu().setWidth("80%");
        addSection("Views:");

        MButton prjButton = new MButton(AppContext.getMessage(ProjectI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
        }).withIcon(FontAwesome.BUILDING);
        addMenuItem(prjButton);

        // Buttons with styling (slightly smaller with left-aligned text)
        MButton activityBtn = new MButton("Activities", clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.MyProjectActivities(this, CurrentProjectVariables.getProjectId()));
        }).withIcon(FontAwesome.INBOX);
        addMenuItem(activityBtn);

        // add more buttons for a more realistic look.
        MButton messageBtn = new MButton(AppContext.getMessage(MessageI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE));
        addMenuItem(messageBtn);

        MButton phaseBtn = new MButton(AppContext.getMessage(MilestoneI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
        addMenuItem(phaseBtn);

        MButton taskBtn = new MButton(AppContext.getMessage(TaskI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new TaskEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));
        addMenuItem(taskBtn);

        MButton bugBtn = new MButton(AppContext.getMessage(BugI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
        addMenuItem(bugBtn);

        MButton userBtn = new MButton("Users", clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER));
        addMenuItem(userBtn);

        addSection("Settings:");

        MButton logoutBtn = new MButton("Logout", clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this));
        }).withIcon(FontAwesome.SIGN_OUT);
        addMenuItem(logoutBtn);
    }
}
