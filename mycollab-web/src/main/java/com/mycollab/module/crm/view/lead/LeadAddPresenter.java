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
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.event.LeadEvent;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadAddPresenter extends CrmGenericPresenter<LeadAddView> {
    private static final long serialVersionUID = 1L;

    public LeadAddPresenter() {
        super(LeadAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleLead>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleLead lead) {
                int leadId = saveLead(lead);
                EventBusFactory.getInstance().post(new LeadEvent.GotoRead(this, leadId));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
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
        CrmModule.navigateItem(CrmTypeConstants.LEAD);
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
            SimpleLead lead = null;

            if (data.getParams() instanceof SimpleLead) {
                lead = (SimpleLead) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
                lead = leadService.findById((Integer) data.getParams(), AppUI.getAccountId());
            }

            if (lead == null) {
                throw new ResourceNotFoundException();
            }

            super.onGo(container, data);
            view.editItem(lead);

            if (lead.getId() == null) {
                AppUI.addFragment("crm/lead/add", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        UserUIContext.getMessage(LeadI18nEnum.SINGLE)));
            } else {
                AppUI.addFragment("crm/lead/edit/" + UrlEncodeDecoder.encode(lead.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(LeadI18nEnum.SINGLE), lead.getLastname()));
            }
        } else {
            throw new SecureAccessException();
        }

    }

    private int saveLead(Lead lead) {
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        lead.setSaccountid(AppUI.getAccountId());
        if (lead.getId() == null) {
            leadService.saveWithSession(lead, UserUIContext.getUsername());
        } else {
            leadService.updateWithSession(lead, UserUIContext.getUsername());
        }
        return lead.getId();
    }
}
