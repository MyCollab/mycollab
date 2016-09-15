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
package com.mycollab.module.crm.view.contact;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.events.ContactEvent;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;

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
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.CONTACT);
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
            SimpleContact contact = null;
            if (data.getParams() instanceof SimpleContact) {
                contact = (SimpleContact) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                contact = contactService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
            }
            if (contact == null) {
                throw new ResourceNotFoundException();
            }
            super.onGo(container, data);
            view.editItem(contact);

            if (contact.getId() == null) {
                MyCollabUI.addFragment("crm/contact/add", UserUIContext.getMessage(GenericI18Enum
                        .BROWSER_ADD_ITEM_TITLE, UserUIContext.getMessage(ContactI18nEnum.SINGLE)));
            } else {
                MyCollabUI.addFragment("crm/contact/edit/" + UrlEncodeDecoder.encode(contact.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(ContactI18nEnum.SINGLE), contact.getLastname()));
            }
        } else {
            throw new SecureAccessException();
        }
    }

    private int saveContact(Contact contact) {
        ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
        contact.setSaccountid(MyCollabUI.getAccountId());
        if (contact.getId() == null) {
            contactService.saveWithSession(contact, UserUIContext.getUsername());
        } else {
            contactService.updateWithSession(contact, UserUIContext.getUsername());
        }
        return contact.getId();
    }
}
