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
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.event.LeadEvent;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class LeadConvertReadPresenter extends CrmGenericPresenter<LeadConvertReadView> {
    private static final long serialVersionUID = 1L;

    public LeadConvertReadPresenter() {
        super(LeadConvertReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleLead>() {
            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new LeadEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleLead data) {
                LeadService contactService = AppContextUtil.getSpringBean(LeadService.class);
                LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = contactService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new LeadEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(SimpleLead data) {
                LeadService contactService = AppContextUtil.getSpringBean(LeadService.class);
                LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = contactService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new LeadEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.LEAD);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_LEAD)) {
            if (data.getParams() instanceof SimpleLead) {
                SimpleLead lead = (SimpleLead) data.getParams();
                super.onGo(container, data);
                view.previewItem(lead);

                MyCollabUI.addFragment(CrmLinkGenerator.generateLeadPreviewLink(lead.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, UserUIContext.getMessage(LeadI18nEnum.SINGLE),
                                lead.getLeadName()));
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
