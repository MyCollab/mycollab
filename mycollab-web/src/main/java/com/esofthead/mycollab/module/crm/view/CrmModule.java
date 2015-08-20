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
package com.esofthead.mycollab.module.crm.view;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.events.*;
import com.esofthead.mycollab.module.crm.i18n.*;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.ModuleHelper;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.ui.ServiceMenu;
import com.esofthead.mycollab.web.IDesktopModule;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Iterator;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class CrmModule extends AbstractPageView implements IDesktopModule {
    private static final long serialVersionUID = 1L;

    private PopupButton addPopupMenu;
    private MHorizontalLayout serviceMenuContainer;
    private ServiceMenu serviceMenu;

    public CrmModule() {
        this.setStyleName("crm-module");
        this.addStyleName("crmContainer");
        ControllerRegistry.addController(new CrmController(this));
    }

    public void gotoCrmDashboard() {
        EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
    }

    public void addView(PageView view) {
        this.removeAllComponents();
        ComponentContainer wrapContainer = view.getWidget();
        this.with(wrapContainer).expand(wrapContainer);
    }

    @Override
    public MHorizontalLayout buildMenu() {
        if (serviceMenuContainer == null) {
            serviceMenuContainer = new MHorizontalLayout();
            serviceMenu = new ServiceMenu();
            serviceMenu.addService(CrmTypeConstants.DASHBOARD, AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_DASHBOARD_HEADER),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            EventBusFactory.getInstance().post(new CrmEvent.GotoHome(this, null));
                        }
                    });

            serviceMenu.addService(CrmTypeConstants.ACCOUNT, AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            EventBusFactory.getInstance().post(new AccountEvent.GotoList(this, null));
                        }
                    });

            serviceMenu.addService(CrmTypeConstants.CONTACT, AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
                        }
                    });

            serviceMenu.addService(CrmTypeConstants.LEAD, AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
                        }
                    });

            serviceMenu.addService(CrmTypeConstants.OPPORTUNITY, AppContext.getMessage(CrmCommonI18nEnum
                    .TOOLBAR_OPPORTUNTIES_HEADER), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                }
            });

            serviceMenu.addService(CrmTypeConstants.CASE, AppContext.getMessage(CrmCommonI18nEnum
                    .TOOLBAR_CASES_HEADER), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null));
                }
            });

            serviceMenuContainer.with(serviceMenu);

            Button.ClickListener listener = new CreateItemListener();

            addPopupMenu = new PopupButton("Quick Add");
            addPopupMenu.setIcon(FontAwesome.PLUS_CIRCLE);
            addPopupMenu.addStyleName("add-btn-popup");
            addPopupMenu.setDirection(Alignment.BOTTOM_LEFT);
            OptionPopupContent popupButtonsControl = new OptionPopupContent().withWidth("150px");

            Button newAccountBtn = new Button(AppContext.getMessage(AccountI18nEnum.BUTTON_NEW_ACCOUNT), listener);
            newAccountBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_ACCOUNT));
            newAccountBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
            popupButtonsControl.addOption(newAccountBtn);

            Button newContactBtn = new Button(AppContext.getMessage(ContactI18nEnum.BUTTON_NEW_CONTACT), listener);
            newContactBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_CONTACT));
            newContactBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
            popupButtonsControl.addOption(newContactBtn);

            Button newCampaignBtn = new Button(AppContext.getMessage(CampaignI18nEnum.BUTTON_NEW_CAMPAIGN), listener);
            newCampaignBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN));
            newCampaignBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
            popupButtonsControl.addOption(newCampaignBtn);

            Button newOpportunityBtn = new Button(AppContext.getMessage(OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY), listener);
            newOpportunityBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
            newOpportunityBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
            popupButtonsControl.addOption(newOpportunityBtn);

            Button newLeadBtn = new Button(AppContext.getMessage(LeadI18nEnum.BUTTON_NEW_LEAD), listener);
            newLeadBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_LEAD));
            newLeadBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
            popupButtonsControl.addOption(newLeadBtn);

            Button newCaseBtn = new Button(AppContext.getMessage(CaseI18nEnum.BUTTON_NEW_CASE), listener);
            newCaseBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_CASE));
            newCaseBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CASE));
            popupButtonsControl.addOption(newCaseBtn);

            Button newTaskBtn = new Button(AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK), listener);
            newTaskBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_TASK));
            newTaskBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.TASK));
            popupButtonsControl.addOption(newTaskBtn);

            Button newCallBtn = new Button(AppContext.getMessage(CallI18nEnum.BUTTON_NEW_CALL), listener);
            newCallBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_CALL));
            newCallBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CALL));
            popupButtonsControl.addOption(newCallBtn);

            Button newMeetingBtn = new Button(AppContext.getMessage(MeetingI18nEnum.BUTTON_NEW_MEETING), listener);
            newMeetingBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_MEETING));
            newMeetingBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.MEETING));
            popupButtonsControl.addOption(newMeetingBtn);

            addPopupMenu.setContent(popupButtonsControl);
            serviceMenuContainer.with(addPopupMenu).withAlign(addPopupMenu, Alignment.MIDDLE_LEFT);
        }
        return serviceMenuContainer;
    }

    private class CreateItemListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            String selectedBtnCaption = "";
            String caption = event.getButton().getCaption();

            if (AppContext.getMessage(AccountI18nEnum.BUTTON_NEW_ACCOUNT).equals(caption)) {
                EventBusFactory.getInstance().post(new AccountEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER);
            } else if (AppContext.getMessage(CampaignI18nEnum.BUTTON_NEW_CAMPAIGN).equals(caption)) {
                EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CAMPAIGNS_HEADER);
            } else if (AppContext.getMessage(CaseI18nEnum.BUTTON_NEW_CASE).equals(caption)) {
                EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CASES_HEADER);
            } else if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(ContactI18nEnum.BUTTON_NEW_CONTACT).equals(caption)) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER);
            } else if (AppContext.getMessage(LeadI18nEnum.BUTTON_NEW_LEAD).equals(caption)) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER);
            } else if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY).equals(caption)) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER);
            } else if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.GotoCalendar(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, null));
            } else if (AppContext.getMessage(CallI18nEnum.BUTTON_NEW_CALL).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.CallAdd(this, null));
            } else if (AppContext.getMessage(MeetingI18nEnum.BUTTON_NEW_MEETING).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingAdd(this, null));
            } else if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_DOCUMENT_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new DocumentEvent.GotoDashboard(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CRMNOTIFICATION_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new CrmSettingEvent.GotoNotificationSetting(this, null));
                selectedBtnCaption = caption;
            }

            addPopupMenu.setPopupVisible(false);

            int i = 0;
            for (Iterator<Component> it = serviceMenu.iterator(); it.hasNext(); ) {
                Button btn = (Button) it.next();
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
                for (Iterator<Component> it = serviceMenu.iterator(); it.hasNext(); ) {
                    Button btn = (Button) it.next();
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
