package com.mycollab.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.event.ActivityEvent;
import com.mycollab.module.crm.event.ContactEvent;
import com.mycollab.module.crm.event.OpportunityEvent;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.service.ContactService;
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
public class ContactReadPresenter extends CrmGenericPresenter<ContactReadView> {
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
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                ContactService ContactService = AppContextUtil.getSpringBean(ContactService.class);
                                ContactService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                                EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
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
            public void onPrint(Object source, SimpleContact data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(CrmTypeConstants.CONTACT, Contact.Field.lastname.name(),
                        ContactDefaultDynaFormLayoutFactory.getForm()));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleContact data) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                ContactSearchCriteria criteria = new ContactSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
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
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                ContactSearchCriteria criteria = new ContactSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN));
                Integer nextId = contactService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, nextId));
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
                    task.setType(CrmTypeConstants.CONTACT);
                    task.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(ContactReadPresenter.this, task));
                } else if (itemId.equals("meeting")) {
                    SimpleMeeting meeting = new SimpleMeeting();
                    meeting.setType(CrmTypeConstants.CONTACT);
                    meeting.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.MeetingEdit(ContactReadPresenter.this, meeting));
                } else if (itemId.equals("call")) {
                    SimpleCall call = new SimpleCall();
                    call.setType(CrmTypeConstants.CONTACT);
                    call.setTypeid(view.getItem().getId());
                    EventBusFactory.getInstance().post(new ActivityEvent.CallEdit(ContactReadPresenter.this, call));
                }
            }
        });

        view.getRelatedOpportunityHandlers().addRelatedListHandler(new AbstractRelatedListHandler<SimpleOpportunity>() {
            @Override
            public void createNewRelatedItem(String itemId) {
                SimpleOpportunity opportunity = new SimpleOpportunity();
                opportunity.setExtraData(view.getItem());
                EventBusFactory.getInstance().post(new OpportunityEvent.GotoEdit(this, opportunity));
            }

            @Override
            public void selectAssociateItems(Set<SimpleOpportunity> items) {
                if (items.size() > 0) {
                    SimpleContact contact = view.getItem();
                    List<ContactOpportunity> associateOpportunities = new ArrayList<ContactOpportunity>();
                    for (SimpleOpportunity opportunity : items) {
                        ContactOpportunity assoOpportunity = new ContactOpportunity();
                        assoOpportunity.setOpportunityid(opportunity.getId());
                        assoOpportunity.setContactid(contact.getId());
                        assoOpportunity.setCreatedtime(new GregorianCalendar().getTime());
                        associateOpportunities.add(assoOpportunity);
                    }

                    ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                    contactService.saveContactOpportunityRelationship(
                            associateOpportunities, AppUI.getAccountId());

                    view.getRelatedOpportunityHandlers().refresh();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CONTACT);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CONTACT)) {
            if (data.getParams() instanceof Integer) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                SimpleContact contact = contactService.findById((Integer) data.getParams(), AppUI.getAccountId());
                if (contact != null) {
                    super.onGo(container, data);
                    view.previewItem(contact);

                    AppUI.addFragment(CrmLinkGenerator.generateContactPreviewLink(contact.getId()),
                            UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                                    UserUIContext.getMessage(ContactI18nEnum.SINGLE), contact.getContactName()));

                } else {
                    throw new ResourceNotFoundException();
                }
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
