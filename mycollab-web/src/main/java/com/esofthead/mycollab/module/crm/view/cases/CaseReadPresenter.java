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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.reporting.FormReportLayout;
import com.esofthead.mycollab.reporting.PrintButton;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractRelatedListHandler;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseReadPresenter extends CrmGenericPresenter<CaseReadView> {
    private static final long serialVersionUID = 1L;

    public CaseReadPresenter() {
        super(CaseReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleCase>() {
            @Override
            public void onEdit(SimpleCase data) {
                EventBusFactory.getInstance().post(new CaseEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleCase data) {
                EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleCase data) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    CaseService caseService = ApplicationContextUtil.getSpringBean(CaseService.class);
                                    caseService.removeWithSession(data,
                                            AppContext.getUsername(), AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new CaseEvent.GotoList(this, null));
                                }
                            }
                        });
            }

            @Override
            public void onPrint(Object source, SimpleCase data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(CrmTypeConstants.CASE, CaseWithBLOBs.Field.subject.name(),
                        CasesDefaultFormLayoutFactory.getForm()));
            }

            @Override
            public void onClone(SimpleCase data) {
                SimpleCase cloneData = (SimpleCase) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new CaseEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new CaseEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleCase data) {
                CaseService caseService = ApplicationContextUtil.getSpringBean(CaseService.class);
                CaseSearchCriteria criteria = new CaseSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                Integer nextId = caseService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new CaseEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleCase data) {
                CaseService caseService = ApplicationContextUtil.getSpringBean(CaseService.class);
                CaseSearchCriteria criteria = new CaseSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
                Integer nextId = caseService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new CaseEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });

        view.getRelatedActivityHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleActivity>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                if (itemId.equals("task")) {
                    SimpleTask task = new SimpleTask();
                    task.setType(CrmTypeConstants.CASE);
                    task.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(CaseReadPresenter.this, task));
                } else if (itemId.equals("meeting")) {
                    SimpleMeeting meeting = new SimpleMeeting();
                    meeting.setType(CrmTypeConstants.CASE);
                    meeting.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(CaseReadPresenter.this, meeting));
                } else if (itemId.equals("call")) {
                    SimpleCall call = new SimpleCall();
                    call.setType(CrmTypeConstants.CASE);
                    call.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(CaseReadPresenter.this, call));
                }
            }
        });

        view.getRelatedContactHandlers().addRelatedListHandler(
                new AbstractRelatedListHandler<SimpleContact>() {
                    @Override
                    public void createNewRelatedItem(String itemId) {
                        SimpleContact contact = new SimpleContact();
                        contact.setExtraData(view.getItem());
                        EventBusFactory.getInstance().post(new ContactEvent.GotoEdit(CaseReadPresenter.this, contact));
                    }

                    @Override
                    public void selectAssociateItems(Set<SimpleContact> items) {
                        List<ContactCase> associateContacts = new ArrayList<>();
                        SimpleCase cases = view.getItem();
                        for (SimpleContact contact : items) {
                            ContactCase associateContact = new ContactCase();
                            associateContact.setCaseid(cases.getId());
                            associateContact.setContactid(contact.getId());
                            associateContact.setCreatedtime(new GregorianCalendar().getTime());
                            associateContacts.add(associateContact);
                        }

                        ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);
                        contactService.saveContactCaseRelationship(associateContacts, AppContext.getAccountId());
                        view.getRelatedContactHandlers().refresh();
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CASE);
        if (AppContext.canRead(RolePermissionCollections.CRM_CASE)) {
            if (data.getParams() instanceof Integer) {
                CaseService caseService = ApplicationContextUtil.getSpringBean(CaseService.class);
                SimpleCase cases = caseService.findById((Integer) data.getParams(), AppContext.getAccountId());
                if (cases != null) {
                    super.onGo(container, data);
                    view.previewItem(cases);

                    AppContext.addFragment(CrmLinkGenerator.generateCasePreviewLink(cases.getId()),
                            AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, "Case", cases.getSubject()));
                } else {
                    throw new ResourceNotFoundException();
                }
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
