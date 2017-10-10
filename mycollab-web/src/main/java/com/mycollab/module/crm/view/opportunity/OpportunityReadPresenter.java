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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.event.ActivityEvent;
import com.mycollab.module.crm.event.ContactEvent;
import com.mycollab.module.crm.event.LeadEvent;
import com.mycollab.module.crm.event.OpportunityEvent;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.vaadin.reporting.FormReportLayout;
import com.mycollab.vaadin.reporting.PrintButton;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.AbstractRelatedListHandler;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunityReadPresenter extends CrmGenericPresenter<OpportunityReadView> {
    private static final long serialVersionUID = 1L;

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
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                OpportunityService OpportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                                OpportunityService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
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
            public void onPrint(Object source, SimpleOpportunity data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(CrmTypeConstants.OPPORTUNITY, Opportunity.Field.opportunityname.name(),
                        OpportunityDefaultDynaFormLayoutFactory.getForm()));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleOpportunity data) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
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
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN));
                Integer nextId = opportunityService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });

        view.getRelatedActivityHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleActivity>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                if (itemId.equals("task")) {
                    SimpleCrmTask task = new SimpleCrmTask();
                    task.setType(CrmTypeConstants.OPPORTUNITY);
                    task.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(OpportunityReadPresenter.this, task));
                } else if (itemId.equals("meeting")) {
                    SimpleMeeting meeting = new SimpleMeeting();
                    meeting.setType(CrmTypeConstants.OPPORTUNITY);
                    meeting.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(OpportunityReadPresenter.this, meeting));
                } else if (itemId.equals("call")) {
                    SimpleCall call = new SimpleCall();
                    call.setType(CrmTypeConstants.OPPORTUNITY);
                    call.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(OpportunityReadPresenter.this, call));
                }
            }
        });

        view.getRelatedContactHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleContactOpportunityRel>() {

            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleContact contact = new SimpleContact();
                contact.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(OpportunityReadPresenter.this, contact));
            }

            @Override
            public void selectAssociateItems(Set<SimpleContactOpportunityRel> items) {
                List<ContactOpportunity> associateContacts = new ArrayList<>();
                SimpleOpportunity opportunity = view.getItem();
                for (SimpleContact contact : items) {
                    ContactOpportunity associateContact = new ContactOpportunity();
                    associateContact.setContactid(contact.getId());
                    associateContact.setOpportunityid(opportunity.getId());
                    associateContact.setCreatedtime(new GregorianCalendar().getTime());
                    associateContacts.add(associateContact);
                }

                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                contactService.saveContactOpportunityRelationship(associateContacts, AppUI.getAccountId());
                view.getRelatedContactHandlers().refresh();
            }
        });

        view.getRelatedLeadHandlers().addRelatedListHandler(
                new AbstractRelatedListHandler<SimpleLead>() {
                    @Override
                    public void createNewRelatedItem(String itemId) {
                        SimpleLead lead = new SimpleLead();
                        lead.setExtraData(view.getItem());
                        EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(OpportunityReadPresenter.this, lead));
                    }

                    @Override
                    public void selectAssociateItems(Set<SimpleLead> items) {
                        SimpleOpportunity opportunity = view.getItem();
                        List<OpportunityLead> associateLeads = new ArrayList<>();
                        for (SimpleLead lead : items) {
                            OpportunityLead associateLead = new OpportunityLead();
                            associateLead.setLeadid(lead.getId());
                            associateLead.setOpportunityid(opportunity.getId());
                            associateLead.setCreatedtime(new GregorianCalendar().getTime());
                            associateLeads.add(associateLead);
                        }

                        OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                        opportunityService.saveOpportunityLeadRelationship(associateLeads, AppUI.getAccountId());
                        view.getRelatedLeadHandlers().refresh();
                    }
                });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.OPPORTUNITY);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
            if (data.getParams() instanceof Integer) {
                OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
                SimpleOpportunity opportunity = opportunityService.findById((Integer) data.getParams(), AppUI.getAccountId());
                if (opportunity != null) {
                    super.onGo(container, data);
                    view.previewItem(opportunity);

                    AppUI.addFragment(CrmLinkGenerator.generateOpportunityPreviewLink(opportunity.getId()),
                            UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                                    UserUIContext.getMessage(OpportunityI18nEnum.SINGLE), opportunity.getOpportunityname()));
                } else {
                    throw new ResourceNotFoundException();
                }
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
