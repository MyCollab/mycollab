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
package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class OpportunityReadPresenter extends AbstractCrmPresenter<OpportunityReadView> {
    private static final long serialVersionUID = -1981281467054000670L;

    public OpportunityReadPresenter() {
        super(OpportunityReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleOpportunity>() {
            @Override
            public void onEdit(SimpleOpportunity data) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleOpportunity data) {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleOpportunity data) {
                ConfirmDialog.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        dialog -> {
                            if (dialog.isConfirmed()) {
                                OpportunityService OpportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                                OpportunityService.removeWithSession(data, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
                            }
                        });
            }

            @Override
            public void onClone(SimpleOpportunity data) {
                SimpleOpportunity cloneData = (SimpleOpportunity) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleOpportunity data) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = opportunityService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(SimpleOpportunity data) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = opportunityService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {

            if (data.getParams() instanceof Integer) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                SimpleOpportunity opportunity = opportunityService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (opportunity != null) {
                    view.previewItem(opportunity);
                    super.onGo(container, data);

                    MyCollabUI.addFragment(CrmLinkGenerator.generateOpportunityPreviewLink(opportunity.getId()), UserUIContext.getMessage(
                            GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, "Opportunity", opportunity.getOpportunityname()));
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
