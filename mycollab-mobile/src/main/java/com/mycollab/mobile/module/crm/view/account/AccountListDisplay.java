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

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.AccountEvent;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountListDisplay extends DefaultPagedBeanList<AccountService, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = 1491890029721763281L;

    public AccountListDisplay() {
        super(AppContextUtil.getSpringBean(AccountService.class), new AccountRowDisplayHandler());
    }

    static public class AccountRowDisplayHandler implements RowDisplayHandler<SimpleAccount> {

        @Override
        public Component generateRow(final SimpleAccount account, int rowIndex) {
            final Button b = new Button(account.getAccountname(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new AccountEvent.GotoRead(this, account.getId()));
                }
            });
            b.setWidth("100%");
            return b;
        }
    }
}
