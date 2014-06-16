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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.common.localization.WebExceptionI18nEnum;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.view.CrmGenericListPresenter;
import com.esofthead.mycollab.module.crm.view.CrmToolbar;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.mvp.MassUpdateCommand;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.MailFormWindow;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ContactListPresenter
		extends
		CrmGenericListPresenter<ContactListView, ContactSearchCriteria, SimpleContact>
		implements MassUpdateCommand<Contact> {

	private static final long serialVersionUID = 1L;
	private ContactService contactService;

	public ContactListPresenter() {
		super(ContactListView.class, ContactListNoItemView.class);
	}

	@Override
	protected void postInitView() {
		super.postInitView();
		contactService = ApplicationContextUtil
				.getSpringBean(ContactService.class);

		view.getPopupActionHandlers().addMassItemActionHandler(
				new DefaultMassEditActionHandler(this) {

					@Override
					protected String getReportTitle() {
						return AppContext
								.getMessage(ContactI18nEnum.LIST_VIEW_TITLE);
					}

					@Override
					protected Class<?> getReportModelClassType() {
						return SimpleContact.class;
					}

					@Override
					protected void onSelectExtra(String id) {
						if ("mail".equals(id)) {
							if (isSelectAll) {
								NotificationUtil.showWarningNotification(AppContext
										.getMessage(WebExceptionI18nEnum.NOT_SUPPORT_SENDING_EMAIL_TO_ALL_USERS));

							} else {
								List<String> lstMail = new ArrayList<String>();
								List<SimpleContact> tableData = view
										.getPagedBeanTable()
										.getCurrentDataList();
								for (SimpleContact item : tableData) {
									if (item.isSelected()) {
										lstMail.add(item.getContactName()
												+ " <" + item.getEmail() + ">");
									}
								}
								UI.getCurrent().addWindow(
										new MailFormWindow(lstMail));
							}

						} else if ("massUpdate".equals(id)) {
							MassUpdateContactWindow massUpdateWindow = new MassUpdateContactWindow(
									AppContext
											.getMessage(
													GenericI18Enum.MASS_UPDATE_WINDOW_TITLE,
													AppContext
															.getMessage(CrmCommonI18nEnum.CONTACT)),
									ContactListPresenter.this);
							UI.getCurrent().addWindow(massUpdateWindow);
						}

					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_CONTACT)) {
			CrmToolbar crmToolbar = ViewManager.getView(CrmToolbar.class);
			crmToolbar.gotoItem(AppContext
					.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER));

			searchCriteria = (ContactSearchCriteria) data.getParams();
			int totalCount = contactService.getTotalCount(searchCriteria);
			if (totalCount > 0) {
				this.displayListView(container, data);
				doSearch(searchCriteria);
			} else {
				this.displayNoExistItems(container, data);
			}

			AppContext.addFragment("crm/contact/list", AppContext.getMessage(
					GenericI18Enum.BROWSER_LIST_ITEMS_TITLE,
					AppContext.getMessage(CrmCommonI18nEnum.CONTACT)));
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	@Override
	protected void deleteSelectedItems() {
		if (!isSelectAll) {
			Collection<SimpleContact> currentDataList = view
					.getPagedBeanTable().getCurrentDataList();
			List<Integer> keyList = new ArrayList<Integer>();
			for (SimpleContact item : currentDataList) {
				if (item.isSelected()) {
					keyList.add(item.getId());
				}
			}

			if (keyList.size() > 0) {
				contactService.massRemoveWithSession(keyList,
						AppContext.getUsername(), AppContext.getAccountId());
				doSearch(searchCriteria);
				checkWhetherEnableTableActionControl();
			}
		} else {
			contactService.removeByCriteria(searchCriteria,
					AppContext.getAccountId());
			doSearch(searchCriteria);
		}
	}

	@Override
	public void massUpdate(Contact value) {
		if (!isSelectAll) {
			Collection<SimpleContact> currentDataList = view
					.getPagedBeanTable().getCurrentDataList();
			List<Integer> keyList = new ArrayList<Integer>();
			for (SimpleContact item : currentDataList) {
				if (item.isSelected()) {
					keyList.add(item.getId());
				}
			}
			if (keyList.size() > 0) {
				contactService.massUpdateWithSession(value, keyList,
						AppContext.getAccountId());
				doSearch(searchCriteria);
			}
		} else {
			contactService.updateBySearchCriteria(value, searchCriteria);
			doSearch(searchCriteria);
		}
	}

	@Override
	public ISearchableService<ContactSearchCriteria> getSearchService() {
		return ApplicationContextUtil.getSpringBean(ContactService.class);
	}
}
