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

package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class OpportunityTableDisplay
        extends
        DefaultPagedBeanTable<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {

    public OpportunityTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    public OpportunityTableDisplay(TableViewField requiredColumn,
                                   List<TableViewField> displayColumns) {
        this(null, requiredColumn, displayColumns);

    }

    public OpportunityTableDisplay(String viewId,
                                   TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(ApplicationContextUtil.getSpringBean(OpportunityService.class),
                SimpleOpportunity.class, viewId, requiredColumn, displayColumns);

        this.addGeneratedColumn("selected", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       Object columnId) {
                final SimpleOpportunity opportunity = getBeanByIndex(itemId);
                final CheckBoxDecor cb = new CheckBoxDecor("", opportunity
                        .isSelected());
                cb.setImmediate(true);
                cb.addValueChangeListener(new Property.ValueChangeListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        OpportunityTableDisplay.this
                                .fireSelectItemEvent(opportunity);
                        fireTableEvent(new TableClickEvent(
                                OpportunityTableDisplay.this, opportunity,
                                "selected"));
                    }
                });

                opportunity.setExtraData(cb);
                return cb;
            }
        });

        this.addGeneratedColumn("opportunityname", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {
                final SimpleOpportunity opportunity = getBeanByIndex(itemId);

                LabelLink b = new LabelLink(opportunity.getOpportunityname(),
                        CrmLinkBuilder
                                .generateOpportunityPreviewLinkFull(opportunity
                                        .getId()));
                if ("Closed Won".equals(opportunity.getSalesstage())
                        || "Closed Lost".equals(opportunity.getSalesstage())) {
                    b.addStyleName(UIConstants.LINK_COMPLETED);
                } else {
                    if (opportunity.isOverdue()) {
                        b.addStyleName(UIConstants.LINK_OVERDUE);
                    }
                }
                b.setDescription(CrmTooltipGenerator
                        .generateTooltipOpportunity(AppContext.getUserLocale(),
                                opportunity, AppContext.getSiteUrl(),
                                AppContext.getTimezone()));

                return b;
            }
        });

        this.addGeneratedColumn("amount", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {
                final SimpleOpportunity opportunity = OpportunityTableDisplay.this
                        .getBeanByIndex(itemId);

                String amount = "";
                if (opportunity.getAmount() != null) {
                    amount = opportunity.getAmount() + "";

                    if (opportunity.getCurrency() != null) {
                        amount += " " + opportunity.getCurrency().getSymbol();
                    }
                }

                return new Label(amount);
            }
        });

        this.addGeneratedColumn("assignUserFullName",
                new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public com.vaadin.ui.Component generateCell(Table source,
                                                                final Object itemId, Object columnId) {
                        final SimpleOpportunity opportunity = OpportunityTableDisplay.this
                                .getBeanByIndex(itemId);
                        return new UserLink(opportunity.getAssignuser(),
                                opportunity.getAssignUserAvatarId(),
                                opportunity.getAssignUserFullName());

                    }
                });

        this.addGeneratedColumn("accountName", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {
                final SimpleOpportunity opportunity = OpportunityTableDisplay.this
                        .getBeanByIndex(itemId);

                return new LabelLink(opportunity.getAccountName(),
                        CrmLinkBuilder
                                .generateAccountPreviewLinkFull(opportunity
                                        .getAccountid()));
            }
        });

        this.addGeneratedColumn("campaignName", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {
                final SimpleOpportunity opportunity = OpportunityTableDisplay.this
                        .getBeanByIndex(itemId);

                return new LabelLink(opportunity.getCampaignName(),
                        CrmLinkBuilder
                                .generateCampaignPreviewLinkFull(opportunity
                                        .getCampaignid()));
            }
        });

        this.addGeneratedColumn("expectedcloseddate",
                new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public com.vaadin.ui.Component generateCell(Table source,
                                                                Object itemId, Object columnId) {
                        final SimpleOpportunity opportunity = getBeanByIndex(itemId);
                        return new ELabel().prettyDate(opportunity.getExpectedcloseddate());
                    }
                });

        this.setWidth("100%");
    }
}
