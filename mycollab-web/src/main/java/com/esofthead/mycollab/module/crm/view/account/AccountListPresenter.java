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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.common.i18n.ErrorI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.view.CrmGenericListPresenter;
import com.esofthead.mycollab.module.crm.view.CrmToolbar;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.events.ViewItemAction;
import com.esofthead.mycollab.vaadin.mvp.MassUpdateCommand;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.MailFormWindow;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class AccountListPresenter extends CrmGenericListPresenter<AccountListView, AccountSearchCriteria, SimpleAccount>
        implements MassUpdateCommand<Account> {
    private static final long serialVersionUID = 1L;

    private AccountService accountService;

    public AccountListPresenter() {
        super(AccountListView.class, AccountCrmListNoItemView.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();
        accountService = ApplicationContextUtil.getSpringBean(AccountService.class);

        view.getPopupActionHandlers().setMassActionHandler(
                new DefaultMassEditActionHandler(this) {
                    @Override
                    protected Class<SimpleAccount> getReportModelClassType() {
                        return SimpleAccount.class;
                    }

                    @Override
                    protected String getReportTitle() {
                        return AppContext.getMessage(AccountI18nEnum.VIEW_LIST_TITLE);
                    }

                    @Override
                    protected void onSelectExtra(String id) {
                        if (ViewItemAction.MAIL_ACTION().equals(id)) {
                            if (isSelectAll) {
                                NotificationUtil.showWarningNotification(AppContext
                                        .getMessage(ErrorI18nEnum.NOT_SUPPORT_SENDING_EMAIL_TO_ALL_USERS));
                            } else {
                                List<String> lstMail = new ArrayList<>();
                                Collection<SimpleAccount> tableData = view
                                        .getPagedBeanTable()
                                        .getCurrentDataList();
                                for (SimpleAccount item : tableData) {
                                    if (item.isSelected()) {
                                        lstMail.add(String.format("%s <%s>", item.getAccountname(), item.getEmail()));
                                    }
                                }
                                UI.getCurrent().addWindow(new MailFormWindow(lstMail));
                            }
                        } else if (ViewItemAction.MASS_UPDATE_ACTION().equals(id)) {
                            MassUpdateAccountWindow massUpdateWindow = new MassUpdateAccountWindow(
                                    AppContext.getMessage(GenericI18Enum.WINDOW_MASS_UPDATE_TITLE,
                                                    AppContext.getMessage(CrmCommonI18nEnum.ACCOUNT)), AccountListPresenter.this);
                            UI.getCurrent().addWindow(massUpdateWindow);
                        }
                    }
                });
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleAccount> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Integer> keyList = new ArrayList<>();
            for (SimpleAccount item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item.getId());
                }
            }

            if (keyList.size() > 0) {
                accountService.massRemoveWithSession(keyList, AppContext.getUsername(), AppContext.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            accountService.removeByCriteria(searchCriteria, AppContext.getAccountId());
            doSearch(searchCriteria);
        }
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmToolbar.navigateItem(CrmTypeConstants.ACCOUNT);
        if (AppContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
            searchCriteria = (AccountSearchCriteria) data.getParams();
            int totalCount = accountService.getTotalCount(searchCriteria);
            if (totalCount > 0) {
                this.displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                this.displayNoExistItems(container, data);
            }

            AppContext.addFragment("crm/account/list", AppContext.getMessage(AccountI18nEnum.VIEW_LIST_TITLE));
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    @Override
    public void massUpdate(Account value) {
        if (!isSelectAll) {
            Collection<SimpleAccount> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Integer> keyList = new ArrayList<>();
            for (SimpleAccount item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item.getId());
                }
            }

            if (keyList.size() > 0) {
                accountService.massUpdateWithSession(value, keyList, AppContext.getAccountId());
                doSearch(searchCriteria);
            }
        } else {
            accountService.updateBySearchCriteria(value, searchCriteria);
            doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<AccountSearchCriteria> getSearchService() {
        return ApplicationContextUtil.getSpringBean(AccountService.class);
    }
}