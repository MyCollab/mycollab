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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.web.ui.LabelLink;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.UserLink;
import com.esofthead.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseTableDisplay extends DefaultPagedBeanTable<CaseService, CaseSearchCriteria, SimpleCase> {

    public CaseTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    public CaseTableDisplay(TableViewField requiredColumn, List<TableViewField> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    public CaseTableDisplay(String viewId, TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(CaseService.class), SimpleCase.class, viewId, requiredColumn, displayColumns);

        this.addGeneratedColumn("selected", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       Object columnId) {
                final SimpleCase cases = getBeanByIndex(itemId);
                CheckBoxDecor cb = new CheckBoxDecor("", cases.isSelected());
                cb.setImmediate(true);
                cb.addValueChangeListener(new ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        CaseTableDisplay.this.fireSelectItemEvent(cases);
                    }
                });

                cases.setExtraData(cb);
                return cb;
            }
        });

        this.addGeneratedColumn("subject", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {
                SimpleCase cases = getBeanByIndex(itemId);
                LabelLink b = new LabelLink(cases.getSubject(), CrmLinkBuilder.generateCasePreviewLinkFull(cases.getId()));

                if (cases.isCompleted()) {
                    b.addStyleName(UIConstants.LINK_COMPLETED);
                }
                b.setDescription(CrmTooltipGenerator.generateTooltipCases(AppContext.getUserLocale(), cases,
                        AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
                return b;
            }
        });

        this.addGeneratedColumn("accountName", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {
                SimpleCase cases = getBeanByIndex(itemId);
                return new LabelLink(cases.getAccountName(), CrmLinkBuilder.generateAccountPreviewLinkFull(cases.getAccountid()));
            }
        });

        this.addGeneratedColumn("assignUserFullName", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source, final Object itemId, Object columnId) {
                SimpleCase cases = getBeanByIndex(itemId);
                return new UserLink(cases.getAssignuser(), cases.getAssignUserAvatarId(), cases.getAssignUserFullName());

            }
        });

        this.addGeneratedColumn("createdtime", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source, Object itemId, Object columnId) {
                SimpleCase cases = getBeanByIndex(itemId);
                return new ELabel(AppContext.formatPrettyTime(cases.getCreatedtime())).withDescription(AppContext
                        .formatDateTime(cases.getCreatedtime()));
            }
        });

        this.setWidth("100%");
    }
}
