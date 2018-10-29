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
package com.mycollab.module.crm.view.cases;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class CaseTableDisplay extends DefaultPagedGrid<CaseService, CaseSearchCriteria, SimpleCase> {

    public CaseTableDisplay(List<GridFieldMeta> displayColumns) {
        this(null, displayColumns);
    }

    public CaseTableDisplay(GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    public CaseTableDisplay(String viewId, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(CaseService.class), SimpleCase.class, viewId, requiredColumn, displayColumns);

//        this.addGeneratedColumn("selected", (source, itemId, columnId) -> {
//            final SimpleCase cases = getBeanByIndex(itemId);
//            CheckBoxDecor cb = new CheckBoxDecor("", cases.isSelected());
//            cb.setImmediate(true);
//            cb.addValueChangeListener(valueChangeEvent -> fireSelectItemEvent(cases));
//            cases.setExtraData(cb);
//            return cb;
//        });
//
//        this.addGeneratedColumn("subject", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            LabelLink b = new LabelLink(cases.getSubject(), CrmLinkGenerator.generateCasePreviewLink(cases.getId()));
//
//            if (cases.isCompleted()) {
//                b.addStyleName(WebThemes.LINK_COMPLETED);
//            }
//            b.setDescription(CrmTooltipGenerator.generateTooltipCases(UserUIContext.getUserLocale(), cases,
//                    AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
//            return b;
//        });
//
//        this.addGeneratedColumn("accountName", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return new LabelLink(cases.getAccountName(), CrmLinkGenerator.generateAccountPreviewLink(cases.getAccountid()));
//        });
//
//        this.addGeneratedColumn("assignUserFullName", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return new UserLink(cases.getAssignuser(), cases.getAssignUserAvatarId(), cases.getAssignUserFullName());
//        });
//
//        this.addGeneratedColumn("createdtime", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return new ELabel(UserUIContext.formatPrettyTime(cases.getCreatedtime())).withDescription(UserUIContext
//                    .formatDateTime(cases.getCreatedtime()));
//        });
//
//        this.addGeneratedColumn("origin", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return ELabel.i18n(cases.getOrigin(), CaseOrigin.class);
//        });
//
//        this.addGeneratedColumn("priority", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return ELabel.i18n(cases.getPriority(), CasePriority.class);
//        });
//
//        this.addGeneratedColumn("status", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return ELabel.i18n(cases.getStatus(), CaseStatus.class);
//        });
//
//        this.addGeneratedColumn("reason", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return ELabel.i18n(cases.getReason(), CaseReason.class);
//        });
//
//        this.addGeneratedColumn("type", (source, itemId, columnId) -> {
//            SimpleCase cases = getBeanByIndex(itemId);
//            return ELabel.i18n(cases.getType(), CaseType.class);
//        });

        this.setWidth("100%");
    }
}
