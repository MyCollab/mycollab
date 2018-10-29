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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class OpportunityTableDisplay extends DefaultPagedGrid<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {

    public OpportunityTableDisplay(List<GridFieldMeta> displayColumns) {
        this(null, displayColumns);
    }

    public OpportunityTableDisplay(GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    public OpportunityTableDisplay(String viewId, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(OpportunityService.class),
                SimpleOpportunity.class, viewId, requiredColumn, displayColumns);

//        this.addGeneratedColumn("selected", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            final CheckBoxDecor cb = new CheckBoxDecor("", opportunity.isSelected());
//            cb.setImmediate(true);
//            cb.addValueChangeListener(valueChangeEvent -> {
//                fireSelectItemEvent(opportunity);
//                fireTableEvent(new TableClickEvent(OpportunityTableDisplay.this, opportunity, "selected"));
//            });
//            opportunity.setExtraData(cb);
//            return cb;
//        });
//
//        this.addGeneratedColumn("opportunityname", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//
//            LabelLink b = new LabelLink(opportunity.getOpportunityname(), CrmLinkGenerator.generateOpportunityPreviewLink(opportunity.getId()));
//            if (OpportunitySalesStage.Closed_Won.name().equals(opportunity.getSalesstage()) ||
//                    OpportunitySalesStage.Closed_Lost.name().equals(opportunity.getSalesstage())) {
//                b.addStyleName(WebThemes.LINK_COMPLETED);
//            } else {
//                if (opportunity.isOverdue()) {
//                    b.addStyleName(WebThemes.LINK_OVERDUE);
//                }
//            }
//            b.setDescription(CrmTooltipGenerator.generateTooltipOpportunity(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
//                    opportunity, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
//
//            return b;
//        });
//
//        this.addGeneratedColumn("amount", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//
//            String amount = "";
//            if (opportunity.getAmount() != null) {
//                amount = opportunity.getAmount() + "";
//
//                if (opportunity.getCurrencyid() != null) {
//                    amount += " " + opportunity.getCurrencyid();
//                }
//            }
//
//            return new Label(amount);
//        });
//
//        this.addGeneratedColumn("assignUserFullName", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            return new UserLink(opportunity.getAssignuser(), opportunity.getAssignUserAvatarId(),
//                    opportunity.getAssignUserFullName());
//        });
//
//        this.addGeneratedColumn("accountName", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            return new LabelLink(opportunity.getAccountName(), CrmLinkGenerator.generateAccountPreviewLink(opportunity.getAccountid()));
//        });
//
//        this.addGeneratedColumn("campaignName", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            return new LabelLink(opportunity.getCampaignName(), CrmLinkGenerator.generateCampaignPreviewLink(opportunity.getCampaignid()));
//        });
//
//        this.addGeneratedColumn("expectedcloseddate", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            return new ELabel().prettyDate(opportunity.getExpectedcloseddate());
//        });
//
//        this.addGeneratedColumn("salesstage", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            return ELabel.i18n(opportunity.getSalesstage(), OpportunitySalesStage.class);
//        });
//
//        this.addGeneratedColumn("opportunitytype", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            return ELabel.i18n(opportunity.getOpportunitytype(), OpportunityType.class);
//        });
//
//        this.addGeneratedColumn("source", (source, itemId, columnId) -> {
//            final SimpleOpportunity opportunity = getBeanByIndex(itemId);
//            return ELabel.i18n(opportunity.getSource(), OpportunityLeadSource.class);
//        });

        this.setWidth("100%");
    }
}
