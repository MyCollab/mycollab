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
package com.esofthead.mycollab.module.crm.view.lead;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.WebExceptionI18nEnum;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.crm.domain.Lead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.view.CrmGenericListPresenter;
import com.esofthead.mycollab.module.crm.view.CrmToolbar;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
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
public class LeadListPresenter extends
		CrmGenericListPresenter<LeadListView, LeadSearchCriteria, SimpleLead>
		implements MassUpdateCommand<Lead> {

	private static final long serialVersionUID = 1L;
	private LeadService leadService;

	public LeadListPresenter() {
		super(LeadListView.class, LeadListNoItemView.class);
	}

	@Override
	protected void postInitView() {
		super.postInitView();
		leadService = ApplicationContextUtil.getSpringBean(LeadService.class);

		view.getPopupActionHandlers().addMassItemActionHandler(
				new DefaultMassEditActionHandler(this) {

					@Override
					protected void onSelectExtra(String id) {
						if (MassItemActionHandler.MAIL_ACTION.equals(id)) {
							if (isSelectAll) {
								NotificationUtil.showWarningNotification(AppContext
										.getMessage(WebExceptionI18nEnum.NOT_SUPPORT_SENDING_EMAIL_TO_ALL_USERS));
							} else {
								List<String> lstMail = new ArrayList<String>();

								List<SimpleLead> tableData = view
										.getPagedBeanTable()
										.getCurrentDataList();
								for (SimpleLead item : tableData) {
									if (item.isSelected()) {
										lstMail.add(item.getLeadName() + " <"
												+ item.getEmail() + ">");
									}
								}

								UI.getCurrent().addWindow(
										new MailFormWindow(lstMail));
							}

						} else if (MassItemActionHandler.MASS_UPDATE_ACTION
								.equals(id)) {
							MassUpdateLeadWindow massUpdateWindow = new MassUpdateLeadWindow(
									AppContext
											.getMessage(
													GenericI18Enum.MASS_UPDATE_WINDOW_TITLE,
													"Lead"),
									LeadListPresenter.this);
							UI.getCurrent().addWindow(massUpdateWindow);
						}

					}

					@Override
					protected String getReportTitle() {
						return AppContext
								.getMessage(LeadI18nEnum.LIST_VIEW_TITLE);
					}

					@Override
					protected Class<?> getReportModelClassType() {
						return SimpleLead.class;
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_LEAD)) {
			CrmToolbar crmToolbar = ViewManager.getView(CrmToolbar.class);
			crmToolbar.gotoItem(AppContext
					.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER));

			searchCriteria = (LeadSearchCriteria) data.getParams();
			int totalCount = leadService.getTotalCount(searchCriteria);
			if (totalCount > 0) {
				this.displayListView(container, data);
				doSearch(searchCriteria);
			} else {
				this.displayNoExistItems(container, data);
			}

			AppContext.addFragment("crm/lead/list", AppContext.getMessage(
					GenericI18Enum.BROWSER_LIST_ITEMS_TITLE, "Lead"));
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	@Override
	protected void deleteSelectedItems() {
		if (!isSelectAll) {
			Collection<SimpleLead> currentDataList = view.getPagedBeanTable()
					.getCurrentDataList();
			List<Integer> keyList = new ArrayList<Integer>();
			for (SimpleLead item : currentDataList) {
				if (item.isSelected()) {
					keyList.add(item.getId());
				}
			}

			if (keyList.size() > 0) {
				leadService.massRemoveWithSession(keyList,
						AppContext.getUsername(), AppContext.getAccountId());
				doSearch(searchCriteria);
				checkWhetherEnableTableActionControl();
			}
		} else {
			leadService.removeByCriteria(searchCriteria,
					AppContext.getAccountId());
			doSearch(searchCriteria);
		}
	}

	@Override
	public void massUpdate(Lead value) {
		if (!isSelectAll) {
			Collection<SimpleLead> currentDataList = view.getPagedBeanTable()
					.getCurrentDataList();
			List<Integer> keyList = new ArrayList<Integer>();
			for (SimpleLead item : currentDataList) {
				if (item.isSelected()) {
					keyList.add(item.getId());
				}
			}

			if (keyList.size() > 0) {
				leadService.massUpdateWithSession(value, keyList,
						AppContext.getAccountId());
				doSearch(searchCriteria);
			}
		} else {
			leadService.updateBySearchCriteria(value, searchCriteria);
			doSearch(searchCriteria);
		}
	}

	@Override
	public ISearchableService<LeadSearchCriteria> getSearchService() {
		return ApplicationContextUtil.getSpringBean(LeadService.class);
	}
}
