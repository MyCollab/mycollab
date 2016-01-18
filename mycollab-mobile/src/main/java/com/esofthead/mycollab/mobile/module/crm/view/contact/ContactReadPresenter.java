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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.RelatedListHandler;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactReadPresenter extends AbstractCrmPresenter<ContactReadView> {
    private static final long serialVersionUID = 1L;

    public ContactReadPresenter() {
        super(ContactReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleContact>() {
            @Override
            public void onEdit(SimpleContact data) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleContact data) {
                EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleContact data) {
                ConfirmDialog.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.CloseListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    ContactService ContactService = ApplicationContextUtil.getSpringBean(ContactService.class);
                                    ContactService.removeWithSession(data,
                                            AppContext.getUsername(), AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
                                }
                            }
                        });
            }

            @Override
            public void onClone(SimpleContact data) {
                SimpleContact cloneData = (SimpleContact) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleContact data) {
                ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);
                ContactSearchCriteria criteria = new ContactSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                Integer nextId = contactService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleContact data) {
                ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);
                ContactSearchCriteria criteria = new ContactSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
                Integer nextId = contactService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
        view.getRelatedOpportunityHandlers().addRelatedListHandler(new RelatedListHandler<SimpleOpportunity>() {

            @Override
            public void selectAssociateItems(Set<SimpleOpportunity> items) {
                SimpleContact contact = view.getItem();
                List<ContactOpportunity> associateOpportunities = new ArrayList<>();
                for (SimpleOpportunity opportunity : items) {
                    ContactOpportunity assoOpportunity = new ContactOpportunity();
                    assoOpportunity.setOpportunityid(opportunity.getId());
                    assoOpportunity.setContactid(contact.getId());
                    assoOpportunity.setCreatedtime(new GregorianCalendar().getTime());
                    associateOpportunities.add(assoOpportunity);
                }

                ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);
                contactService.saveContactOpportunityRelationship(
                        associateOpportunities, AppContext.getAccountId());

                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleOpportunity opportunity = new SimpleOpportunity();
                opportunity.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(ContactReadPresenter.this, opportunity));
            }
        });
        view.getRelatedActivityHandlers().addRelatedListHandler(new RelatedListHandler<SimpleActivity>() {
            @Override
            public void selectAssociateItems(Set<SimpleActivity> items) {

            }

            @Override
            public void createNewRelatedItem(String itemId) {
                if (itemId.equals(CrmTypeConstants.TASK)) {
                    SimpleTask task = new SimpleTask();
                    task.setType(CrmTypeConstants.ACCOUNT);
                    task.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(ContactReadPresenter.this, task));
                } else if (itemId.equals(CrmTypeConstants.MEETING)) {
                    final SimpleMeeting meeting = new SimpleMeeting();
                    meeting.setType(CrmTypeConstants.ACCOUNT);
                    meeting.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(ContactReadPresenter.this, meeting));
                } else if (itemId.equals(CrmTypeConstants.CALL)) {
                    final SimpleCall call = new SimpleCall();
                    call.setType(CrmTypeConstants.ACCOUNT);
                    call.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(ContactReadPresenter.this, call));
                }
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canRead(RolePermissionCollections.CRM_CONTACT)) {
            /*
             * CrmNavigationMenu crmToolbar = (CrmNavigationMenu) container
			 * .getNavigationMenu(); crmToolbar.selectButton(AppContext
			 * .getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER));
			 */

            if (data.getParams() instanceof Integer) {
                ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);
                SimpleContact contact = contactService.findById((Integer) data.getParams(), AppContext.getAccountId());
                if (contact != null) {
                    view.previewItem(contact);
                    super.onGo(container, data);

                    AppContext.addFragment(CrmLinkGenerator.generateContactPreviewLink(contact.getId()),
                            AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, "Contact", contact.getContactName()));

                } else {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }
}
