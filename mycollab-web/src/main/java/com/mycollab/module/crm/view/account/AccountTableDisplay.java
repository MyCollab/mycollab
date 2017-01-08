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

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.OptionI18nEnum.AccountIndustry;
import com.mycollab.module.crm.i18n.OptionI18nEnum.AccountType;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.CheckBoxDecor;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.mycollab.vaadin.web.ui.UrlLink;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.ui.Label;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AccountTableDisplay extends DefaultPagedBeanTable<AccountService, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = 1L;

    public AccountTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    public AccountTableDisplay(TableViewField requiredColumn, List<TableViewField> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    public AccountTableDisplay(String viewId, TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(AccountService.class),
                SimpleAccount.class, viewId, requiredColumn, displayColumns);

        addGeneratedColumn("selected", (source, itemId, columnId) -> {
            final SimpleAccount account = getBeanByIndex(itemId);
            final CheckBoxDecor cb = new CheckBoxDecor("", account.isSelected());
            cb.addValueChangeListener(valueChangeEvent -> {
                fireSelectItemEvent(account);
                fireTableEvent(new TableClickEvent(AccountTableDisplay.this, account, "selected"));
            });
            account.setExtraData(cb);
            return cb;
        });

        addGeneratedColumn("email", (source, itemId, columnId) -> {
            SimpleAccount account = getBeanByIndex(itemId);
            return ELabel.email(account.getEmail());
        });

        addGeneratedColumn("accountname", (source, itemId, columnId) -> {
            SimpleAccount account = getBeanByIndex(itemId);
            LabelLink b = new LabelLink(account.getAccountname(), CrmLinkBuilder.generateAccountPreviewLinkFull(account.getId()));
            b.setDescription(CrmTooltipGenerator.generateToolTipAccount(UserUIContext.getUserLocale(), account, MyCollabUI.getSiteUrl()));
            return b;
        });

        addGeneratedColumn("assignUserFullName", (source, itemId, columnId) -> {
            SimpleAccount account = getBeanByIndex(itemId);
            return new UserLink(account.getAssignuser(), account.getAssignUserAvatarId(), account.getAssignUserFullName());
        });

        addGeneratedColumn("industry", (source, itemId, columnId) -> {
            SimpleAccount account = getBeanByIndex(itemId);
            return ELabel.i18n(account.getIndustry(), AccountIndustry.class);
        });

        addGeneratedColumn("type", (source, itemId, columnId) -> {
            SimpleAccount account = getBeanByIndex(itemId);
            return ELabel.i18n(account.getType(), AccountType.class);
        });

        addGeneratedColumn("website", (source, itemId, columnId) -> {
            SimpleAccount account = getBeanByIndex(itemId);
            if (account.getWebsite() != null) {
                return new UrlLink(account.getWebsite());
            } else {
                return new Label("");
            }
        });

        this.setWidth("100%");
    }
}
