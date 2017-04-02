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
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.view.CrmGenericListPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.ViewItemAction;
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
import java.util.stream.Collectors;

/**
 * @author MyCollab Ltd.
 * @since 2.0
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
        accountService = AppContextUtil.getSpringBean(AccountService.class);

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {
            @Override
            protected Class<SimpleAccount> getReportModelClassType() {
                return SimpleAccount.class;
            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(AccountI18nEnum.LIST);
            }

            @Override
            protected void onSelectExtra(String id) {
                if (ViewItemAction.MAIL_ACTION().equals(id)) {
                    if (isSelectAll) {
                        NotificationUtil.showWarningNotification(UserUIContext.getMessage(ErrorI18nEnum.NOT_SUPPORT_SENDING_EMAIL_TO_ALL_USERS));
                    } else {
                        List<String> lstMail = new ArrayList<>();
                        Collection<SimpleAccount> tableData = view.getPagedBeanTable().getCurrentDataList();
                        for (SimpleAccount item : tableData) {
                            if (item.isSelected()) {
                                lstMail.add(String.format("%s <%s>", item.getAccountname(), item.getEmail()));
                            }
                        }
                        UI.getCurrent().addWindow(new MailFormWindow(lstMail));
                    }
                } else if (ViewItemAction.MASS_UPDATE_ACTION().equals(id)) {
                    MassUpdateAccountWindow massUpdateWindow = new MassUpdateAccountWindow(
                            UserUIContext.getMessage(GenericI18Enum.WINDOW_MASS_UPDATE_TITLE,
                                    UserUIContext.getMessage(AccountI18nEnum.LIST)), AccountListPresenter.this);
                    UI.getCurrent().addWindow(massUpdateWindow);
                }
            }
        });
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleAccount> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Account> keyList = currentDataList.stream().filter(item -> item.isSelected()).collect(Collectors.toList());

            if (keyList.size() > 0) {
                accountService.massRemoveWithSession(keyList, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            accountService.removeByCriteria(searchCriteria, MyCollabUI.getAccountId());
            doSearch(searchCriteria);
        }
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.ACCOUNT);
        if (UserUIContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
            searchCriteria = (AccountSearchCriteria) data.getParams();
            int totalCount = accountService.getTotalCount(searchCriteria);
            if (totalCount > 0) {
                this.displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                this.displayNoExistItems(container, data);
            }

            MyCollabUI.addFragment("crm/account/list", UserUIContext.getMessage(AccountI18nEnum.LIST));
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    public void massUpdate(Account value) {
        if (!isSelectAll) {
            Collection<SimpleAccount> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Integer> keyList = currentDataList.stream().filter(item -> item.isSelected()).map(item -> item.getId())
                    .collect(Collectors.toList());

            if (keyList.size() > 0) {
                accountService.massUpdateWithSession(value, keyList, MyCollabUI.getAccountId());
                doSearch(searchCriteria);
            }
        } else {
            accountService.updateBySearchCriteria(value, searchCriteria);
            doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<AccountSearchCriteria> getSearchService() {
        return AppContextUtil.getSpringBean(AccountService.class);
    }
}