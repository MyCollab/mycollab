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
package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class AccountListPresenter extends CrmListPresenter<AccountListView, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = -3014478937143932048L;

    public AccountListPresenter() {
        super(AccountListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        super.onGo(container, data);
        MyCollabUI.addFragment("crm/account/list", UserUIContext.getMessage(AccountI18nEnum.LIST));
    }

}
