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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
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
public class OpportunityAddPresenter extends CrmGenericPresenter<OpportunityAddView> {
    private static final long serialVersionUID = 1L;

    public OpportunityAddPresenter() {
        super(OpportunityAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new EditFormHandler<SimpleOpportunity>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleOpportunity item) {
                int opportunityId = saveOpportunity(item);
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, opportunityId));
            }

            @Override
            public void onCancel() {
                ViewState viewState = HistoryViewManager.back();
                if (viewState instanceof NullViewState) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                }
            }

            @Override
            public void onSaveAndNew(final SimpleOpportunity item) {
                saveOpportunity(item);
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.OPPORTUNITY);
        if (AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
            SimpleOpportunity opportunity = null;
            if (data.getParams() instanceof SimpleOpportunity) {
                opportunity = (SimpleOpportunity) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                OpportunityService accountService = ApplicationContextUtil.getSpringBean(OpportunityService.class);
                opportunity = accountService.findById((Integer) data.getParams(), AppContext.getAccountId());
            }
            if (opportunity == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
            super.onGo(container, data);
            view.editItem(opportunity);

            if (opportunity.getId() == null) {
                AppContext.addFragment("crm/opportunity/add", AppContext
                        .getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE, "Opportunity"));
            } else {
                AppContext.addFragment("crm/opportunity/edit/" + UrlEncodeDecoder.encode(opportunity.getId()),
                        AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE, "Opportunity",
                                opportunity.getOpportunityname()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private int saveOpportunity(Opportunity opportunity) {
        OpportunityService opportunityService = ApplicationContextUtil.getSpringBean(OpportunityService.class);

        opportunity.setSaccountid(AppContext.getAccountId());
        if (opportunity.getId() == null) {
            opportunityService.saveWithSession(opportunity, AppContext.getUsername());
        } else {
            opportunityService.updateWithSession(opportunity, AppContext.getUsername());
        }
        return opportunity.getId();
    }
}
