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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
class BugTableDisplay extends DefaultPagedGrid<BugService, BugSearchCriteria, SimpleBug> {
    private static final long serialVersionUID = 1L;

    BugTableDisplay(List<GridFieldMeta> displayColumns) {
        this(null, displayColumns);
    }

    BugTableDisplay(GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    BugTableDisplay(String viewId, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(BugService.class), SimpleBug.class, viewId, requiredColumn, displayColumns);

//        this.addGeneratedColumn("assignuserFullName", (source, itemId, columnId) -> {
//            SimpleBug bug = getBeanByIndex(itemId);
//            return new ProjectUserLink(bug.getProjectid(), bug.getAssignuser(), bug.getAssignUserAvatarId(), bug.getAssignuserFullName());
//        });
//
//        this.addGeneratedColumn("loguserFullName", (source, itemId, columnId) -> {
//            SimpleBug bug = getBeanByIndex(itemId);
//            return new ProjectUserLink(bug.getProjectid(), bug.getCreateduser(), bug.getLoguserAvatarId(), bug.getLoguserFullName());
//        });
//
//        this.addGeneratedColumn("name", (source, itemId, columnId) -> {
//            SimpleBug bug = getBeanByIndex(itemId);
//            LabelLink b = new LabelLink(bug.getName(), ProjectLinkGenerator.generateBugPreviewLink(bug.getBugkey(),
//                    bug.getProjectShortName()));
//
//            if (StringUtils.isNotBlank(bug.getPriority())) {
//                b.setIconLink(ProjectAssetsManager.getPriority(bug.getPriority()));
//                b.addStyleName("priority-" + bug.getPriority().toLowerCase());
//            }
//
//            b.setDescription(ProjectTooltipGenerator.generateToolTipBug(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
//                    bug, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone(), false));
//
//            if (bug.isCompleted()) {
//                b.addStyleName(WebThemes.LINK_COMPLETED);
//            } else if (bug.isOverdue()) {
//                b.addStyleName(WebThemes.LINK_OVERDUE);
//            }
//            return b;
//        });
//
//        this.addGeneratedColumn("severity", (source, itemId, columnId) -> {
//            SimpleBug bug = getBeanByIndex(itemId);
//            Label lbPriority = new Label(UserUIContext.getMessage(BugSeverity.class, bug.getSeverity()));
//            lbPriority.setIcon(FontAwesome.STAR);
//            if (bug.getSeverity() != null) {
//                lbPriority.addStyleName("bug-severity-" + bug.getSeverity().toLowerCase());
//            }
//            return lbPriority;
//        });
//
//        this.addGeneratedColumn("duedate", (source, itemId, columnId) -> {
//            SimpleBug bug = getBeanByIndex(itemId);
//            return new ELabel().prettyDate(bug.getDuedate());
//        });
//
//        this.addGeneratedColumn("createdtime", (source, itemId, columnId) -> {
//            SimpleBug bug = getBeanByIndex(itemId);
//            return new ELabel().prettyDateTime(bug.getCreatedtime());
//        });
//
//        this.addGeneratedColumn("resolution", (source, itemId, columnId) -> {
//            SimpleBug bug = getBeanByIndex(itemId);
//            return new Label(UserUIContext.getMessage(BugResolution.class, bug.getResolution()));
//        });
        this.setWidth("100%");
    }
}
