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

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

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

        addGeneratedColumn("selected", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       final Object columnId) {
                final SimpleAccount account = getBeanByIndex(itemId);
                final CheckBoxDecor cb = new CheckBoxDecor("", account.isSelected());
                cb.addValueChangeListener(new ValueChangeListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        fireSelectItemEvent(account);
                        fireTableEvent(new TableClickEvent(AccountTableDisplay.this, account, "selected"));

                    }
                });
                account.setExtraData(cb);
                return cb;
            }
        });

        addGeneratedColumn("email", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component generateCell(Table source, Object itemId, Object columnId) {
                SimpleAccount account = getBeanByIndex(itemId);
                return new EmailLink(account.getEmail());
            }
        });

        addGeneratedColumn("accountname", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component generateCell(Table source, Object itemId, Object columnId) {
                SimpleAccount account = getBeanByIndex(itemId);
                LabelLink b = new LabelLink(account.getAccountname(), CrmLinkBuilder.generateAccountPreviewLinkFull(account.getId()));
                b.setDescription(CrmTooltipGenerator.generateToolTipAccount(AppContext.getUserLocale(), account, AppContext.getSiteUrl()));
                return b;
            }
        });

        addGeneratedColumn("assignUserFullName", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component generateCell(Table source, Object itemId, Object columnId) {
                SimpleAccount account = getBeanByIndex(itemId);
                return new UserLink(account.getAssignuser(), account.getAssignUserAvatarId(), account.getAssignUserFullName());

            }
        });

        addGeneratedColumn("website", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(final Table source,
                                                        final Object itemId, final Object columnId) {
                SimpleAccount account = getBeanByIndex(itemId);
                if (account.getWebsite() != null) {
                    return new UrlLink(account.getWebsite());
                } else {
                    return new Label("");
                }

            }
        });

        this.setWidth("100%");
    }
}
