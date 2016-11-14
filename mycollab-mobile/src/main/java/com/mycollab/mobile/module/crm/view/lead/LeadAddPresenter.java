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
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.LeadEvent;
import com.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class LeadAddPresenter extends AbstractCrmPresenter<LeadAddView> {
    private static final long serialVersionUID = -873280663492245087L;

    public LeadAddPresenter() {
        super(LeadAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<SimpleLead>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleLead lead) {
                saveLead(lead);
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void onSaveAndNew(final SimpleLead lead) {
                saveLead(lead);
                EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
            SimpleLead lead = null;
            if (data.getParams() instanceof SimpleLead) {
                lead = (SimpleLead) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                lead = leadService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
            }
            if (lead == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
            super.onGo(container, data);
            view.editItem(lead);

            if (lead.getId() == null) {
                MyCollabUI.addFragment("crm/lead/add", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        UserUIContext.getMessage(LeadI18nEnum.SINGLE)));
            } else {
                MyCollabUI.addFragment("crm/lead/edit/" + UrlEncodeDecoder.encode(lead.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(LeadI18nEnum.SINGLE), lead.getLastname()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }

    private void saveLead(Lead lead) {
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        lead.setSaccountid(MyCollabUI.getAccountId());
        if (lead.getId() == null) {
            leadService.saveWithSession(lead, UserUIContext.getUsername());
        } else {
            leadService.updateWithSession(lead, UserUIContext.getUsername());
        }
    }
}
