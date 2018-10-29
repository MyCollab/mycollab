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
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class LeadTableDisplay extends DefaultPagedGrid<LeadService, LeadSearchCriteria, SimpleLead> {

    public LeadTableDisplay(List<GridFieldMeta> displayColumns) {
        this(null, displayColumns);
    }

    public LeadTableDisplay(GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        this(null, requiredColumn, displayColumns);

    }

    public LeadTableDisplay(String viewId, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(LeadService.class),
                SimpleLead.class, viewId, requiredColumn, displayColumns);

//        this.addGeneratedColumn("selected", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//            final CheckBoxDecor cb = new CheckBoxDecor("", lead.isSelected());
//            cb.setImmediate(true);
//            cb.addValueChangeListener(valueChangeEvent -> {
//                fireSelectItemEvent(lead);
//                fireTableEvent(new TableClickEvent(LeadTableDisplay.this, lead, "selected"));
//            });
//            lead.setExtraData(cb);
//            return cb;
//        });
//
//        this.addGeneratedColumn("leadName", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//
//            LabelLink b = new LabelLink(lead.getLeadName(), CrmLinkGenerator.generateLeadPreviewLink(lead.getId()));
//            if ("Dead".equals(lead.getStatus()) || "Converted".equals(lead.getStatus())) {
//                b.addStyleName(WebThemes.LINK_COMPLETED);
//            }
//            b.setDescription(CrmTooltipGenerator.generateTooltipLead(
//                    UserUIContext.getUserLocale(), lead,
//                    AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
//            return b;
//        });
//
//        this.addGeneratedColumn("assignUserFullName", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//            return new UserLink(lead.getAssignuser(), lead.getAssignUserAvatarId(), lead.getAssignUserFullName());
//        });
//
//        this.addGeneratedColumn("email", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//            Link l = new Link();
//            l.setResource(new ExternalResource("mailto:" + lead.getEmail()));
//            l.setCaption(lead.getEmail());
//            return l;
//        });
//
//        this.addGeneratedColumn("status", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//            return ELabel.i18n(lead.getStatus(), LeadStatus.class);
//        });
//
//        this.addGeneratedColumn("industry", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//            return ELabel.i18n(lead.getIndustry(), AccountIndustry.class);
//        });
//
//        this.addGeneratedColumn("source", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//            return ELabel.i18n(lead.getSource(), OpportunityLeadSource.class);
//        });
//
//        this.addGeneratedColumn("website", (source, itemId, columnId) -> {
//            final SimpleLead lead = getBeanByIndex(itemId);
//            if (lead.getWebsite() != null) {
//                return new UrlLink(lead.getWebsite());
//            } else {
//                return new Label("");
//            }
//        });

        this.setWidth("100%");
    }
}
