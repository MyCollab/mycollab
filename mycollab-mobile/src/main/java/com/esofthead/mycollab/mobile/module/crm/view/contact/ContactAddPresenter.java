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

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactAddPresenter extends AbstractCrmPresenter<ContactAddView> {
    private static final long serialVersionUID = -2859144864540984138L;

    public ContactAddPresenter() {
        super(ContactAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<SimpleContact>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleContact contact) {
                saveContact(contact);
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
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
        if (AppContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
            SimpleContact contact = null;
            if (data.getParams() instanceof SimpleContact) {
                contact = (SimpleContact) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                contact = contactService.findById((Integer) data.getParams(), AppContext.getAccountId());
            }
            if (contact == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
            super.onGo(container, data);
            view.editItem(contact);

            if (contact.getId() == null) {
                AppContext.addFragment("crm/contact/add", AppContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        AppContext.getMessage(ContactI18nEnum.SINGLE)));
            } else {
                AppContext.addFragment("crm/contact/edit/" + UrlEncodeDecoder.encode(contact.getId()),
                        AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                AppContext.getMessage(ContactI18nEnum.SINGLE), contact.getLastname()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private void saveContact(Contact contact) {
        ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
        contact.setSaccountid(AppContext.getAccountId());
        if (contact.getId() == null) {
            contactService.saveWithSession(contact, AppContext.getUsername());
        } else {
            contactService.updateWithSession(contact, AppContext.getUsername());
        }
    }

}
