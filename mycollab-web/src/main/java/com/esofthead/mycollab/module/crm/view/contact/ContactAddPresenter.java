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
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.service.ContactService;
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
public class ContactAddPresenter extends CrmGenericPresenter<ContactAddView> {
    private static final long serialVersionUID = 1L;

    public ContactAddPresenter() {
        super(ContactAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new EditFormHandler<SimpleContact>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleContact contact) {
                int contactId = saveContact(contact);
                EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, contactId));
            }

            @Override
            public void onCancel() {
                ViewState viewState = HistoryViewManager.back();
                if (viewState instanceof NullViewState) {
                    EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null));
                }
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
        if (AppContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
            SimpleContact contact = null;
            if (data.getParams() instanceof SimpleContact) {
                contact = (SimpleContact) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);
                contact = contactService.findById((Integer) data.getParams(), AppContext.getAccountId());
            }
            if (contact == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
            super.onGo(container, data);
            view.editItem(contact);

            if (contact.getId() == null) {
                AppContext.addFragment("crm/contact/add", AppContext
                        .getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE, "Contact"));
            } else {
                AppContext.addFragment(
                        "crm/contact/edit/"
                                + UrlEncodeDecoder.encode(contact.getId()),
                        AppContext.getMessage(
                                GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                "Contact", contact.getLastname()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private int saveContact(Contact contact) {
        ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);

        contact.setSaccountid(AppContext.getAccountId());
        if (contact.getId() == null) {
            contactService.saveWithSession(contact, AppContext.getUsername());
        } else {
            contactService.updateWithSession(contact, AppContext.getUsername());
        }
        return contact.getId();
    }
}
