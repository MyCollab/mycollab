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
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class CampaignTableDisplay
        extends
        DefaultPagedBeanTable<CampaignService, CampaignSearchCriteria, SimpleCampaign> {

    public CampaignTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    public CampaignTableDisplay(TableViewField requiredColumn,
                                List<TableViewField> displayColumns) {
        this(null, requiredColumn, displayColumns);

    }

    public CampaignTableDisplay(String viewId, TableViewField requiredColumn,
                                List<TableViewField> displayColumns) {
        super(ApplicationContextUtil.getSpringBean(CampaignService.class),
                SimpleCampaign.class, viewId, requiredColumn, displayColumns);
        this.addGeneratedColumn("selected", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       Object columnId) {
                final SimpleCampaign campaign = CampaignTableDisplay.this
                        .getBeanByIndex(itemId);
                final CheckBoxDecor cb = new CheckBoxDecor("", campaign
                        .isSelected());
                cb.addValueChangeListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        CampaignTableDisplay.this.fireSelectItemEvent(campaign);
                        fireTableEvent(new TableClickEvent(
                                CampaignTableDisplay.this, campaign, "selected"));

                    }
                });

                campaign.setExtraData(cb);
                return cb;
            }
        });

        this.addGeneratedColumn("campaignname", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleCampaign campaign = CampaignTableDisplay.this
                        .getBeanByIndex(itemId);

                LabelLink b = new LabelLink(campaign.getCampaignname(),
                        CrmLinkBuilder.generateCampaignPreviewLinkFull(campaign
                                .getId()));
                b.setDescription(CrmTooltipGenerator.generateTooltipCampaign(
                        AppContext.getUserLocale(), campaign,
                        AppContext.getSiteUrl(), AppContext.getTimezone()));
                b.setStyleName("link");

                if ("Complete".equals(campaign.getStatus())) {
                    b.addStyleName(UIConstants.LINK_COMPLETED);
                } else {
                    if (campaign.getEnddate() != null
                            && (campaign.getEnddate()
                            .before(new GregorianCalendar().getTime()))) {
                        b.addStyleName(UIConstants.LINK_OVERDUE);
                    }
                }
                return b;

            }
        });

        this.addGeneratedColumn("assignUserFullName",
                new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public com.vaadin.ui.Component generateCell(Table source,
                                                                final Object itemId, Object columnId) {
                        final SimpleCampaign campaign = CampaignTableDisplay.this
                                .getBeanByIndex(itemId);
                        UserLink b = new UserLink(campaign.getAssignuser(),
                                campaign.getAssignUserAvatarId(), campaign
                                .getAssignUserFullName());
                        return b;

                    }
                });

        this.addGeneratedColumn("expectedrevenue", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleCampaign campaign = CampaignTableDisplay.this
                        .getBeanByIndex(itemId);
                if (campaign.getExpectedrevenue() != null) {
                    String expectedRevenueText = campaign.getExpectedrevenue()
                            + "";
                    Currency currency = campaign.getCurrency();
                    if (currency != null) {
                        expectedRevenueText += " " + currency.getSymbol();
                    }

                    return new Label(expectedRevenueText);
                } else {
                    return new Label("");
                }

            }
        });

        this.addGeneratedColumn("expectedcost", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                final SimpleCampaign campaign = CampaignTableDisplay.this
                        .getBeanByIndex(itemId);
                if (campaign.getExpectedrevenue() != null) {
                    String expectedCostText = campaign.getExpectedcost() + "";
                    Currency currency = campaign.getCurrency();
                    if (currency != null) {
                        expectedCostText += " " + currency.getSymbol();
                    }

                    return new Label(expectedCostText);
                } else {
                    return new Label("");
                }

            }
        });

        this.addGeneratedColumn("startdate", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        Object itemId, Object columnId) {
                final SimpleCampaign campaign = getBeanByIndex(itemId);
                return new ELabel().prettyDate(campaign.getStartdate());
            }
        });

        this.addGeneratedColumn("enddate", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        Object itemId, Object columnId) {
                final SimpleCampaign campaign = getBeanByIndex(itemId);
                return new ELabel().prettyDate(campaign.getEnddate());
            }
        });

        this.setWidth("100%");
    }
}
