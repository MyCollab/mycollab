/**
 * This file is part of mycollab-mobile.
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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmNavigationMenu;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ContactReadPresenter extends CrmGenericPresenter<ContactReadView> {

	private static final long serialVersionUID = 1L;

	public ContactReadPresenter() {
		super(ContactReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleContact>() {
					@Override
					public void onEdit(SimpleContact data) {
						EventBus.getInstance().fireEvent(
								new ContactEvent.GotoEdit(this, data));
					}

					@Override
					public void onAdd(SimpleContact data) {
						EventBus.getInstance().fireEvent(
								new ContactEvent.GotoAdd(this, null));
					}

					@Override
					public void onDelete(final SimpleContact data) {

						ConfirmDialog.show(
								UI.getCurrent(),
								AppContext
										.getMessage(GenericI18Enum.CONFIRM_DELETE_RECORD_DIALOG_MESSAGE),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
								new ConfirmDialog.CloseListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											ContactService ContactService = ApplicationContextUtil
													.getSpringBean(ContactService.class);
											ContactService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											EventBus.getInstance().fireEvent(
													new ContactEvent.GotoList(
															this, null));
										}
									}
								});
					}

					@Override
					public void onClone(SimpleContact data) {
						SimpleContact cloneData = (SimpleContact) data.copy();
						cloneData.setId(null);
						EventBus.getInstance().fireEvent(
								new ContactEvent.GotoEdit(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBus.getInstance().fireEvent(
								new ContactEvent.GotoList(this, null));
					}

					@Override
					public void gotoNext(SimpleContact data) {
						ContactService contactService = ApplicationContextUtil
								.getSpringBean(ContactService.class);
						ContactSearchCriteria criteria = new ContactSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.GREATER));
						Integer nextId = contactService
								.getNextItemKey(criteria);
						if (nextId != null) {
							EventBus.getInstance().fireEvent(
									new ContactEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoLastRecordNotification();
						}

					}

					@Override
					public void gotoPrevious(SimpleContact data) {
						ContactService contactService = ApplicationContextUtil
								.getSpringBean(ContactService.class);
						ContactSearchCriteria criteria = new ContactSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.LESSTHAN));
						Integer nextId = contactService
								.getPreviousItemKey(criteria);
						if (nextId != null) {
							EventBus.getInstance().fireEvent(
									new ContactEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoFirstRecordNotification();
						}
					}
				});
	}

	@Override
	protected void onGo(MobileNavigationManager container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_CONTACT)) {
			CrmNavigationMenu crmToolbar = (CrmNavigationMenu) container
					.getNavigationMenu();
			crmToolbar.selectButton(AppContext
					.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER));

			if (data.getParams() instanceof Integer) {
				ContactService contactService = ApplicationContextUtil
						.getSpringBean(ContactService.class);
				SimpleContact contact = contactService.findById(
						(Integer) data.getParams(), AppContext.getAccountId());
				if (contact != null) {
					super.onGo(container, data);
					view.previewItem(contact);

					AppContext.addFragment(CrmLinkGenerator
							.generateContactPreviewLink(contact.getId()),
							AppContext.getMessage(
									GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
									"Contact", contact.getContactName()));

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
