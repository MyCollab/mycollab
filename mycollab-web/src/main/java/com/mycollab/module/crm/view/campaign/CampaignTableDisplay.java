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
package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class CampaignTableDisplay extends DefaultPagedGrid<CampaignService, CampaignSearchCriteria, SimpleCampaign> {

    public CampaignTableDisplay(List<GridFieldMeta> displayColumns) {
        this(null, displayColumns);
    }

    public CampaignTableDisplay(GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    public CampaignTableDisplay(String viewId, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(CampaignService.class), SimpleCampaign.class, viewId, requiredColumn, displayColumns);
//        this.addGeneratedColumn("selected", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            final CheckBoxDecor cb = new CheckBoxDecor("", campaign.isSelected());
//            cb.addValueChangeListener(valueChangeEvent -> {
//                fireSelectItemEvent(campaign);
//                fireTableEvent(new TableClickEvent(CampaignTableDisplay.this, campaign, "selected"));
//            });
//            campaign.setExtraData(cb);
//            return cb;
//        });
//
//        this.addGeneratedColumn("campaignname", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//
//            LabelLink b = new LabelLink(campaign.getCampaignname(), CrmLinkGenerator.generateCampaignPreviewLink(campaign.getId()));
//            b.setDescription(CrmTooltipGenerator.generateTooltipCampaign(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
//                    campaign, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
//            b.setStyleName(WebThemes.BUTTON_LINK);
//
//            if (Completed.name().equals(campaign.getStatus())) {
//                b.addStyleName(WebThemes.LINK_COMPLETED);
//            } else {
//                if (campaign.getEnddate() != null && (campaign.getEnddate().before(new GregorianCalendar().getTime()))) {
//                    b.addStyleName(WebThemes.LINK_OVERDUE);
//                }
//            }
//            return b;
//        });
//
//        this.addGeneratedColumn("assignUserFullName", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            return new UserLink(campaign.getAssignuser(), campaign.getAssignUserAvatarId(), campaign.getAssignUserFullName());
//        });
//
//        this.addGeneratedColumn("expectedrevenue", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            if (campaign.getExpectedrevenue() != null) {
//                String expectedRevenueText = campaign.getExpectedrevenue() + "";
//                if (campaign.getCurrencyid() != null) {
//                    expectedRevenueText += " " + campaign.getCurrencyid();
//                }
//
//                return new Label(expectedRevenueText);
//            } else {
//                return new Label("");
//            }
//        });
//
//        this.addGeneratedColumn("expectedcost", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            if (campaign.getExpectedrevenue() != null) {
//                String expectedCostText = campaign.getExpectedcost() + "";
//                if (campaign.getCurrencyid() != null) {
//                    expectedCostText += " " + campaign.getCurrencyid();
//                }
//
//                return new Label(expectedCostText);
//            } else {
//                return new Label("");
//            }
//        });
//
//        this.addGeneratedColumn("startdate", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            return new ELabel().prettyDate(campaign.getStartdate());
//        });
//
//        this.addGeneratedColumn("enddate", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            return new ELabel().prettyDate(campaign.getEnddate());
//        });
//
//        this.addGeneratedColumn("type", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            return ELabel.i18n(campaign.getType(), CampaignType.class);
//        });
//
//        this.addGeneratedColumn("status", (source, itemId, columnId) -> {
//            final SimpleCampaign campaign = getBeanByIndex(itemId);
//            return ELabel.i18n(campaign.getStatus(), CampaignStatus.class);
//        });

        this.setWidth("100%");
    }
}
