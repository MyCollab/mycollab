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

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.module.crm.view.cases.CaseListDisplay;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.vaadin.AppContext;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

public class AccountRelatedCaseView extends AbstractRelatedListView<SimpleCase, CaseSearchCriteria> {
    private static final long serialVersionUID = -4559344487784697088L;
    private Account account;

    public AccountRelatedCaseView() {
        initUI();
    }

    private void initUI() {
        this.setCaption(AppContext.getMessage(CaseI18nEnum.M_TITLE_RELATED_CASES));
        itemList = new CaseListDisplay();
        this.setContent(itemList);
    }

    public void displayCases(final Account account) {
        this.account = account;
        loadCases();
    }

    private void loadCases() {
        final CaseSearchCriteria criteria = new CaseSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setAccountId(new NumberSearchField(account.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadCases();
    }

    @Override
    protected Component createRightComponent() {
        return new MButton("", clickEvent -> fireNewRelatedItem("")).withStyleName("add-btn");
    }

}
