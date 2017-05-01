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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.*;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.FontAwesome;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ProjectMobileMenuPageView extends AbstractMobileMenuPageView {
    @Override
    protected void buildNavigateMenu() {
        getMenu().setWidth("80%");
        addSection("Views");

        MButton prjButton = new MButton(UserUIContext.getMessage(ProjectI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
        }).withIcon(FontAwesome.BUILDING);
        addMenuItem(prjButton);

        // Buttons with styling (slightly smaller with left-aligned text)
        MButton activityBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.MyProjectActivities(this, CurrentProjectVariables.getProjectId()));
        }).withIcon(FontAwesome.INBOX);
        addMenuItem(activityBtn);

        // add more buttons for a more realistic look.
        MButton messageBtn = new MButton(UserUIContext.getMessage(MessageI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE));
        addMenuItem(messageBtn);

        MButton phaseBtn = new MButton(UserUIContext.getMessage(MilestoneI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
        addMenuItem(phaseBtn);

        MButton ticketBtn = new MButton(UserUIContext.getMessage(TicketI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TICKET));
        addMenuItem(ticketBtn);

        MButton userBtn = new MButton(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
        }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER));
        addMenuItem(userBtn);

        addSection("Modules");
        MButton crmModuleBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.MODULE_CRM), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
        }).withIcon(VaadinIcons.MONEY);
        addMenuItem(crmModuleBtn);

        addSection(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));

        MButton logoutBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this));
        }).withIcon(FontAwesome.SIGN_OUT);
        addMenuItem(logoutBtn);
    }
}
