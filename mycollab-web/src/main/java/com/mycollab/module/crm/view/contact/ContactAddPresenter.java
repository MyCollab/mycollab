package com.mycollab.module.crm.view.contact;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.event.ContactEvent;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.service.ContactService;
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
public class ContactAddPresenter extends CrmGenericPresenter<ContactAddView> {
    private static final long serialVersionUID = 1L;

    public ContactAddPresenter() {
        super(ContactAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleContact>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleContact contact) {
                int contactId = saveContact(contact);
                EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, contactId));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(final SimpleContact contact) {
                saveContact(contact);
                EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CONTACT);
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
            SimpleContact contact = null;
            if (data.getParams() instanceof SimpleContact) {
                contact = (SimpleContact) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                contact = contactService.findById((Integer) data.getParams(), AppUI.getAccountId());
            }
            if (contact == null) {
                throw new ResourceNotFoundException();
            }
            super.onGo(container, data);
            view.editItem(contact);

            if (contact.getId() == null) {
                AppUI.addFragment("crm/contact/add", UserUIContext.getMessage(GenericI18Enum
                        .BROWSER_ADD_ITEM_TITLE, UserUIContext.getMessage(ContactI18nEnum.SINGLE)));
            } else {
                AppUI.addFragment("crm/contact/edit/" + UrlEncodeDecoder.encode(contact.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(ContactI18nEnum.SINGLE), contact.getLastname()));
            }
        } else {
            throw new SecureAccessException();
        }
    }

    private int saveContact(Contact contact) {
        ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
        contact.setSaccountid(AppUI.getAccountId());
        if (contact.getId() == null) {
            contactService.saveWithSession(contact, UserUIContext.getUsername());
        } else {
            contactService.updateWithSession(contact, UserUIContext.getUsername());
        }
        return contact.getId();
    }
}
