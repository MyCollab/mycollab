/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.crm.event.*;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.FontAwesome;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class CrmMobileMenuPageView extends AbstractMobileMenuPageView {
    @Override
    protected void buildNavigateMenu() {
        getMenu().setWidth("80%");
        addSection("Views");

        MButton accountBtn = new MButton(UserUIContext.getMessage(AccountI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new AccountEvent.GotoList(this, null));
        }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
        addMenuItem(accountBtn);

        // Buttons with styling (slightly smaller with left-aligned text)
        MButton contactBtn = new MButton(UserUIContext.getMessage(ContactI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
        }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        addMenuItem(contactBtn);

        // add more buttons for a more realistic look.
        MButton campaignBtn = new MButton(UserUIContext.getMessage(CampaignI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new CampaignEvent.GotoList(this, null));
        }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
        addMenuItem(campaignBtn);

        MButton leadBtn = new MButton(UserUIContext.getMessage(LeadI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
        }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
        addMenuItem(leadBtn);

        MButton opportunityBtn = new MButton(UserUIContext.getMessage(OpportunityI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
        }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
        addMenuItem(opportunityBtn);

        MButton caseBtn = new MButton(UserUIContext.getMessage(CaseI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new CaseEvent.GotoList(this, null));
        }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CASE));
        addMenuItem(caseBtn);

        addSection("Modules");
        MButton projectModuleBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.MODULE_PROJECT), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
        }).withIcon(VaadinIcons.TASKS);
        addMenuItem(projectModuleBtn);

        addSection(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));

        MButton logoutBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this));
        }).withIcon(FontAwesome.SIGN_OUT);
        addMenuItem(logoutBtn);
    }
}
