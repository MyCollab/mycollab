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
package com.mycollab.module.crm.view;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.events.*;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.*;
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
        this.with(view).expand(view);
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

            serviceMenu.addService(CrmTypeConstants.ACCOUNT, AppContext.getMessage(AccountI18nEnum.LIST),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            EventBusFactory.getInstance().post(new AccountEvent.GotoList(this, null));
                        }
                    });

            serviceMenu.addService(CrmTypeConstants.CONTACT, AppContext.getMessage(ContactI18nEnum.LIST),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
                        }
                    });

            serviceMenu.addService(CrmTypeConstants.LEAD, AppContext.getMessage(LeadI18nEnum.LIST),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
                        }
                    });

            serviceMenu.addService(CrmTypeConstants.CAMPAIGN, AppContext.getMessage(CampaignI18nEnum.LIST), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new CampaignEvent.GotoList(this, null));
                }
            });

            serviceMenu.addService(CrmTypeConstants.OPPORTUNITY, AppContext.getMessage(OpportunityI18nEnum.LIST), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                }
            });

            serviceMenu.addService(CrmTypeConstants.CASE, AppContext.getMessage(CaseI18nEnum.LIST), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new CaseEvent.GotoList(this, null));
                }
            });

            serviceMenuContainer.with(serviceMenu);

            Button.ClickListener listener = new CreateItemListener();

            addPopupMenu = new PopupButton("Quick Add");
            addPopupMenu.setIcon(FontAwesome.PLUS_CIRCLE);
            addPopupMenu.addStyleName("add-btn-popup");
            addPopupMenu.setDirection(Alignment.BOTTOM_LEFT);
            OptionPopupContent popupButtonsControl = new OptionPopupContent();

            if (AppContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) {
                Button newAccountBtn = new Button(AppContext.getMessage(AccountI18nEnum.NEW), listener);
                newAccountBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
                popupButtonsControl.addOption(newAccountBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
                Button newContactBtn = new Button(AppContext.getMessage(ContactI18nEnum.NEW), listener);
                newContactBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
                popupButtonsControl.addOption(newContactBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN)) {
                Button newCampaignBtn = new Button(AppContext.getMessage(CampaignI18nEnum.NEW), listener);
                newCampaignBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
                popupButtonsControl.addOption(newCampaignBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
                Button newOpportunityBtn = new Button(AppContext.getMessage(OpportunityI18nEnum.NEW), listener);
                newOpportunityBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
                popupButtonsControl.addOption(newOpportunityBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
                Button newLeadBtn = new Button(AppContext.getMessage(LeadI18nEnum.NEW), listener);
                newLeadBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
                popupButtonsControl.addOption(newLeadBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_CASE)) {
                Button newCaseBtn = new Button(AppContext.getMessage(CaseI18nEnum.NEW), listener);
                newCaseBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CASE));
                popupButtonsControl.addOption(newCaseBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_TASK)) {
                Button newTaskBtn = new Button(AppContext.getMessage(TaskI18nEnum.NEW), listener);
                newTaskBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.TASK));
                popupButtonsControl.addOption(newTaskBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_CALL)) {
                Button newCallBtn = new Button(AppContext.getMessage(CallI18nEnum.NEW), listener);
                newCallBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CALL));
                popupButtonsControl.addOption(newCallBtn);
            }

            if (AppContext.canWrite(RolePermissionCollections.CRM_MEETING)) {
                Button newMeetingBtn = new Button(AppContext.getMessage(MeetingI18nEnum.NEW), listener);
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

            if (AppContext.getMessage(AccountI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new AccountEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(AccountI18nEnum.LIST);
            } else if (AppContext.getMessage(CampaignI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CampaignI18nEnum.LIST);
            } else if (AppContext.getMessage(CaseI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(CaseI18nEnum.LIST);
            } else if (AppContext.getMessage(ContactI18nEnum.LIST).equals(caption)) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(ContactI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(ContactI18nEnum.LIST);
            } else if (AppContext.getMessage(LeadI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(LeadI18nEnum.LIST);
            } else if (AppContext.getMessage(LeadI18nEnum.LIST).equals(caption)) {
                EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(OpportunityI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(this, null));
                selectedBtnCaption = AppContext.getMessage(OpportunityI18nEnum.LIST);
            } else if (AppContext.getMessage(OpportunityI18nEnum.LIST).equals(caption)) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.GotoCalendar(this, null));
                selectedBtnCaption = caption;
            } else if (AppContext.getMessage(TaskI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, null));
            } else if (AppContext.getMessage(CallI18nEnum.NEW).equals(caption)) {
                EventBusFactory.getInstance().post(new ActivityEvent.CallAdd(this, null));
            } else if (AppContext.getMessage(MeetingI18nEnum.NEW).equals(caption)) {
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
