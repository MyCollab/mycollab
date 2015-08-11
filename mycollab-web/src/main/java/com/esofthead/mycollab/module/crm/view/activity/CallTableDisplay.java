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

package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.criteria.CallSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CallService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import java.util.List;

/**
 * @author MyCollab Ltd.
 */
public class CallTableDisplay extends
        DefaultPagedBeanTable<CallService, CallSearchCriteria, SimpleCall> {
    private static final long serialVersionUID = 1L;

    public CallTableDisplay(TableViewField requireColumn,
                            List<TableViewField> displayColumns) {
        super(ApplicationContextUtil.getSpringBean(CallService.class), SimpleCall.class,
                requireColumn, displayColumns);

        this.addGeneratedColumn("subject", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleCall call = CallTableDisplay.this
                        .getBeanByIndex(itemId);

                LabelLink b = new LabelLink(call.getSubject(),
                        CrmLinkBuilder.generateCallPreviewLinkFul(call
                                .getId()));

                if ("Held".equals(call.getStatus())) {
                    b.addStyleName(UIConstants.LINK_COMPLETED);
                }
                return b;

            }
        });

        this.addGeneratedColumn("isClosed", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleCall call = CallTableDisplay.this
                        .getBeanByIndex(itemId);
                Button b = new Button(null, new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        fireTableEvent(new TableClickEvent(
                                CallTableDisplay.this, call, "isClosed"));
                    }
                });
                b.setIcon(FontAwesome.TRASH_O);
                b.setStyleName(UIConstants.THEME_LINK);
                b.setDescription("Close this call");
                return b;

            }
        });

        this.addGeneratedColumn("startdate", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleCall call = CallTableDisplay.this
                        .getBeanByIndex(itemId);
                return new Label(AppContext.formatDateTime(call.getStartdate()));

            }
        });
    }
}
