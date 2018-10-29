/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.fielddef.AccountTableFieldDef;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountListDashlet extends Depot {
    private static final long serialVersionUID = 1L;
    private AccountTableDisplay grid;

    public AccountListDashlet() {
        super(UserUIContext.getMessage(AccountI18nEnum.MY_ITEMS), new VerticalLayout());
        this.setMargin(new MarginInfo(true, false, false, false));
        grid = new AccountTableDisplay(Arrays.asList(AccountTableFieldDef.accountname,
                AccountTableFieldDef.phoneoffice, AccountTableFieldDef.email));
        bodyContent.addComponent(grid);

        MButton customizeViewBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(new AccountListCustomizeWindow(grid)))
                .withIcon(VaadinIcons.ADJUST).withStyleName(WebThemes.BUTTON_SMALL_PADDING);
        customizeViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));
        this.addHeaderElement(customizeViewBtn);
    }

    public void display() {
        final AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setAssignUser(StringSearchField.and(UserUIContext.getUsername()));
        grid.setSearchCriteria(criteria);
    }
}
