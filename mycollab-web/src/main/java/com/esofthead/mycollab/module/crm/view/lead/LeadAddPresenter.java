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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Lead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.IEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

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
                ViewState viewState = HistoryViewManager.back();
                if (viewState instanceof NullViewState) {
                    EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
                }
            }

            @Override
            public void onSaveAndNew(final SimpleLead lead) {
                saveLead(lead);
                EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.LEAD);
        if (AppContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
            SimpleLead lead = null;

            if (data.getParams() instanceof SimpleLead) {
                lead = (SimpleLead) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                LeadService leadService = ApplicationContextUtil.getSpringBean(LeadService.class);
                lead = leadService.findById((Integer) data.getParams(), AppContext.getAccountId());
            }

            if (lead == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }

            super.onGo(container, data);
            view.editItem(lead);

            if (lead.getId() == null) {
                AppContext.addFragment("crm/lead/add", AppContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE, "Lead"));
            } else {
                AppContext.addFragment("crm/lead/edit/" + UrlEncodeDecoder.encode(lead.getId()),
                        AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE, "Lead", lead.getLastname()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }

    private int saveLead(Lead lead) {
        LeadService leadService = ApplicationContextUtil.getSpringBean(LeadService.class);
        lead.setSaccountid(AppContext.getAccountId());
        if (lead.getId() == null) {
            leadService.saveWithSession(lead, AppContext.getUsername());
        } else {
            leadService.updateWithSession(lead, AppContext.getUsername());
        }
        return lead.getId();
    }
}
