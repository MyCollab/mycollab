/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.module.crm.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.event.*;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractSingleContainerPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.IModule;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.ModuleHelper;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.ServiceMenu;
import com.mycollab.web.IDesktopModule;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class CrmModule extends AbstractSingleContainerPageView implements IDesktopModule {
    private static final long serialVersionUID = 1L;

    private PopupButton addPopupMenu;
    private MHorizontalLayout serviceMenuContainer;
    private ServiceMenu serviceMenu;

    public CrmModule() {
        addStyleName("module");
        ControllerRegistry.addController(new CrmController(this));
    }

    public void gotoCrmDashboard() {
        EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
    }

    @Override
    public MHorizontalLayout buildMenu() {
        if (serviceMenuContainer == null) {
            serviceMenuContainer = new MHorizontalLayout();
            serviceMenu = new ServiceMenu();
            serviceMenu.addService(CrmTypeConstants.DASHBOARD, UserUIContext.getMessage(CrmCommonI18nEnum.TOOLBAR_DASHBOARD_HEADER),
                    clickEvent -> EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null)));

            serviceMenu.addService(CrmTypeConstants.ACCOUNT, UserUIContext.getMessage(AccountI18nEnum.LIST),
                    clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GotoList(this, null)));

            serviceMenu.addService(CrmTypeConstants.CONTACT, UserUIContext.getMessage(ContactI18nEnum.LIST),
                    clickEvent -> EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null)));

            serviceMenu.addService(CrmTypeConstants.LEAD, UserUIContext.getMessage(LeadI18nEnum.LIST),
                    clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null)));

            serviceMenu.addService(CrmTypeConstants.CAMPAIGN, UserUIContext.getMessage(CampaignI18nEnum.LIST),
                    clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GotoList(this, null)));

            serviceMenu.addService(CrmTypeConstants.OPPORTUNITY, UserUIContext.getMessage(OpportunityI18nEnum.LIST),
                    clickEvent -> EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null)));

            serviceMenu.addService(CrmTypeConstants.CASE, UserUIContext.getMessage(CaseI18nEnum.LIST),
                    clickEvent -> EventBusFactory.getInstance().post(new CaseEvent.GotoList(this, null)));

            serviceMenuContainer.with(serviceMenu);

            Button.ClickListener listener = new CreateItemListener();

            addPopupMenu = new PopupButton(UserUIContext.getMessage(GenericI18Enum.ACTION_NEW));
            addPopupMenu.setIcon(FontAwesome.PLUS_CIRCLE);
            addPopupMenu.addStyleName("add-btn-popup");
            addPopupMenu.setDirection(Alignment.BOTTOM_LEFT);
            OptionPopupContent popupButtonsControl = new OptionPopupContent();

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) {
                Button newAccountBtn = new Button(UserUIContext.getMessage(AccountI18nEnum.SINGLE), listener);
                newAccountBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
                popupButtonsControl.addOption(newAccountBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
                Button newContactBtn = new Button(UserUIContext.getMessage(ContactI18nEnum.SINGLE), listener);
                newContactBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
                popupButtonsControl.addOption(newContactBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN)) {
                Button newCampaignBtn = new Button(UserUIContext.getMessage(CampaignI18nEnum.SINGLE), listener);
                newCampaignBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
                popupButtonsControl.addOption(newCampaignBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
                Button newOpportunityBtn = new Button(UserUIContext.getMessage(OpportunityI18nEnum.SINGLE), listener);
                newOpportunityBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
                popupButtonsControl.addOption(newOpportunityBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
                Button newLeadBtn = new Button(UserUIContext.getMessage(LeadI18nEnum.SINGLE), listener);
                newLeadBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
                popupButtonsControl.addOption(newLeadBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_CASE)) {
                Button newCaseBtn = new Button(UserUIContext.getMessage(CaseI18nEnum.SINGLE), listener);
                newCaseBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CASE));
                popupButtonsControl.addOption(newCaseBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_TASK)) {
                Button newTaskBtn = new Button(UserUIContext.getMessage(TaskI18nEnum.SINGLE), listener);
                newTaskBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.TASK));
                popupButtonsControl.addOption(newTaskBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_CALL)) {
                Button newCallBtn = new Button(UserUIContext.getMessage(CallI18nEnum.SINGLE), listener);
                newCallBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CALL));
                popupButtonsControl.addOption(newCallBtn);
            }

            if (UserUIContext.canWrite(RolePermissionCollections.CRM_MEETING)) {
                Button newMeetingBtn = new Button(UserUIContext.getMessage(MeetingI18nEnum.SINGLE), listener);
                newMeetingBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.MEETING));
                popupButtonsControl.addOption(newMeetingBtn);
            }

            if (popupButtonsControl.getComponentCount() > 0) {
                addPopupMenu.setContent(popupButtonsControl);
                serviceMenuContainer.with(addPopupMenu).withAlign(addPopupMenu, Alignment.MIDDLE_LEFT);
            }
        }
        return serviceMenuContainer;
    }

    private class CreateItemListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            String selectedBtnCaption = "";
            String caption = event.getButton().getCaption();

            if (UserUIContext.getMessage(AccountI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new AccountEvent.GotoAdd(this, null));
                selectedBtnCaption = UserUIContext.getMessage(AccountI18nEnum.LIST);
            } else if (UserUIContext.getMessage(CampaignI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null));
                selectedBtnCaption = UserUIContext.getMessage(CampaignI18nEnum.LIST);
            } else if (UserUIContext.getMessage(CaseI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null));
                selectedBtnCaption = UserUIContext.getMessage(CaseI18nEnum.LIST);
            } else if (UserUIContext.getMessage(ContactI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (UserUIContext.getMessage(ContactI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null));
                selectedBtnCaption = UserUIContext.getMessage(ContactI18nEnum.LIST);
            } else if (UserUIContext.getMessage(LeadI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null));
                selectedBtnCaption = UserUIContext.getMessage(LeadI18nEnum.LIST);
            } else if (UserUIContext.getMessage(LeadI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (UserUIContext.getMessage(OpportunityI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(this, null));
                selectedBtnCaption = UserUIContext.getMessage(OpportunityI18nEnum.LIST);
            } else if (UserUIContext.getMessage(OpportunityI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (UserUIContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.GotoCalendar(this, null));
                selectedBtnCaption = caption;
            } else if (UserUIContext.getMessage(TaskI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, null));
            } else if (UserUIContext.getMessage(CallI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.CallAdd(this, null));
            } else if (UserUIContext.getMessage(MeetingI18nEnum.SINGLE).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingAdd(this, null));
            } else if (UserUIContext.getMessage(CrmCommonI18nEnum.TOOLBAR_DOCUMENT_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new DocumentEvent.GotoDashboard(this, null));
                selectedBtnCaption = caption;
            } else if (UserUIContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CRMNOTIFICATION_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new CrmSettingEvent.GotoNotificationSetting(this, null));
                selectedBtnCaption = caption;
            }

            addPopupMenu.setPopupVisible(false);

            int i = 0;
            for (Component aServiceMenu : serviceMenu) {
                Button btn = (Button) aServiceMenu;
                if (selectedBtnCaption.equals(btn.getCaption())) {
                    serviceMenu.selectService(i);
                    break;
                }
                i++;
            }
        }
    }

    public static void navigateItem(String type) {
        IModule module = ModuleHelper.getCurrentModule();
        if (module != null && module instanceof CrmModule) {
            CrmModule crmModule = (CrmModule) module;
            if (crmModule.serviceMenu != null) {
                ServiceMenu serviceMenu = crmModule.serviceMenu;
                int i = 0;
                for (Component aServiceMenu : serviceMenu) {
                    Button btn = (Button) aServiceMenu;
                    if (type.equals(btn.getId())) {
                        serviceMenu.selectService(i);
                        break;
                    }
                    i++;
                }
            }
        }
    }
}
