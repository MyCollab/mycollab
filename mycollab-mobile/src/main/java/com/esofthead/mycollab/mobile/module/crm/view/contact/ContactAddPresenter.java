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

import java.util.Arrays;
import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.domain.CampaignContact;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.ContactCase;
import com.esofthead.mycollab.module.crm.domain.ContactOpportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.addon.touchkit.ui.NavigationManager;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ContactAddPresenter extends CrmGenericPresenter<ContactAddView> {
	private static final long serialVersionUID = -2859144864540984138L;

	public ContactAddPresenter() {
		super(ContactAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleContact>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSave(final SimpleContact contact) {
						saveContact(contact);
						ViewState viewState = HistoryViewManager.back();

						if (viewState instanceof NullViewState) {
							EventBus.getInstance().fireEvent(
									new ContactEvent.GotoList(this, null));
						}

					}

					@Override
					public void onCancel() {
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBus.getInstance().fireEvent(
									new ContactEvent.GotoList(this, null));
						}
					}

					@Override
					public void onSaveAndNew(final SimpleContact contact) {
						saveContact(contact);
						EventBus.getInstance().fireEvent(
								new ContactEvent.GotoAdd(this, null));
					}
				});
	}

	@Override
	protected void onGo(NavigationManager container, ScreenData<?> data) {
		if (AppContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {

			SimpleContact contact = null;
			if (data.getParams() instanceof SimpleContact) {
				contact = (SimpleContact) data.getParams();
			} else if (data.getParams() instanceof Integer) {
				ContactService contactService = ApplicationContextUtil
						.getSpringBean(ContactService.class);
				contact = contactService.findById((Integer) data.getParams(),
						AppContext.getAccountId());
				if (contact == null) {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}
			super.onGo(container, data);
			view.editItem(contact);

			if (contact.getId() == null) {
				AppContext.addFragment("crm/contact/add", AppContext
						.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
								"Contact"));
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

	public void saveContact(Contact contact) {
		ContactService contactService = ApplicationContextUtil
				.getSpringBean(ContactService.class);

		contact.setSaccountid(AppContext.getAccountId());
		if (contact.getId() == null) {
			contactService.saveWithSession(contact, AppContext.getUsername());

			if (contact.getExtraData() != null
					&& contact.getExtraData() instanceof SimpleCampaign) {
				CampaignContact associateContact = new CampaignContact();
				associateContact.setCampaignid(((SimpleCampaign) contact
						.getExtraData()).getId());
				associateContact.setContactid(contact.getId());
				associateContact.setCreatedtime(new GregorianCalendar()
						.getTime());

				CampaignService campaignService = ApplicationContextUtil
						.getSpringBean(CampaignService.class);
				campaignService.saveCampaignContactRelationship(
						Arrays.asList(associateContact),
						AppContext.getAccountId());
			} else if (contact.getExtraData() != null
					&& contact.getExtraData() instanceof SimpleOpportunity) {
				ContactOpportunity associateContact = new ContactOpportunity();
				associateContact.setContactid(contact.getId());
				associateContact.setOpportunityid(((SimpleOpportunity) contact
						.getExtraData()).getId());
				associateContact.setCreatedtime(new GregorianCalendar()
						.getTime());

				contactService.saveContactOpportunityRelationship(
						Arrays.asList(associateContact),
						AppContext.getAccountId());
			} else if (contact.getExtraData() != null
					&& contact.getExtraData() instanceof SimpleCase) {
				ContactCase associateCase = new ContactCase();
				associateCase.setContactid(contact.getId());
				associateCase.setCaseid(((SimpleCase) contact.getExtraData())
						.getId());
				associateCase.setCreatedtime(new GregorianCalendar().getTime());

				contactService
						.saveContactCaseRelationship(
								Arrays.asList(associateCase),
								AppContext.getAccountId());
			}

		} else {
			contactService.updateWithSession(contact, AppContext.getUsername());
		}
	}

}
