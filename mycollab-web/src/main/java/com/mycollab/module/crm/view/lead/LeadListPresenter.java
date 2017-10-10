/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.view.CrmGenericListPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.ViewItemAction;
import com.mycollab.vaadin.mvp.MassUpdateCommand;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadListPresenter extends CrmGenericListPresenter<LeadListView, LeadSearchCriteria, SimpleLead>
        implements MassUpdateCommand<Lead> {
    private static final long serialVersionUID = 1L;

    private LeadService leadService;

    public LeadListPresenter() {
        super(LeadListView.class, LeadCrmListNoItemView.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();
        leadService = AppContextUtil.getSpringBean(LeadService.class);

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {

            @Override
            protected void onSelectExtra(String id) {
                if (ViewItemAction.MAIL_ACTION.equals(id)) {
                    if (isSelectAll) {
                        NotificationUtil.showWarningNotification(UserUIContext.getMessage(ErrorI18nEnum.NOT_SUPPORT_SENDING_EMAIL_TO_ALL_USERS));
                    } else {
                        List<String> lstMail = new ArrayList<>();
                        Collection<SimpleLead> tableData = view.getPagedBeanTable().getCurrentDataList();
                        for (SimpleLead item : tableData) {
                            if (item.isSelected()) {
                                lstMail.add(String.format("%s <%s>", item.getLeadName(), item.getEmail()));
                            }
                        }

                        UI.getCurrent().addWindow(new MailFormWindow(lstMail));
                    }

                } else if (ViewItemAction.MASS_UPDATE_ACTION.equals(id)) {
                    MassUpdateLeadWindow massUpdateWindow = new MassUpdateLeadWindow(UserUIContext.getMessage(
                            GenericI18Enum.WINDOW_MASS_UPDATE_TITLE, UserUIContext.getMessage(LeadI18nEnum.LIST)),
                            LeadListPresenter.this);
                    UI.getCurrent().addWindow(massUpdateWindow);
                }
            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(LeadI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleLead.class;
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.LEAD);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_LEAD)) {
            searchCriteria = (LeadSearchCriteria) data.getParams();
            int totalCount = leadService.getTotalCount(searchCriteria);
            if (totalCount > 0) {
                this.displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                this.displayNoExistItems(container, data);
            }

            AppUI.addFragment("crm/lead/list", UserUIContext.getMessage(LeadI18nEnum.LIST));
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleLead> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Lead> keyList = new ArrayList<>();
            for (SimpleLead item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item);
                }
            }

            if (keyList.size() > 0) {
                leadService.massRemoveWithSession(keyList, UserUIContext.getUsername(), AppUI.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            leadService.removeByCriteria(searchCriteria, AppUI.getAccountId());
            doSearch(searchCriteria);
        }
    }

    @Override
    public void massUpdate(Lead value) {
        if (!isSelectAll) {
            Collection<SimpleLead> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Integer> keyList = new ArrayList<>();
            for (SimpleLead item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item.getId());
                }
            }

            if (keyList.size() > 0) {
                leadService.massUpdateWithSession(value, keyList, AppUI.getAccountId());
                doSearch(searchCriteria);
            }
        } else {
            leadService.updateBySearchCriteria(value, searchCriteria);
            doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<LeadSearchCriteria> getSearchService() {
        return AppContextUtil.getSpringBean(LeadService.class);
    }
}
