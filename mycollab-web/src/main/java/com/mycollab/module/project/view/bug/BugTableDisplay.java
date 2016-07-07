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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.TableViewField;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugTableDisplay extends DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> {
    private static final long serialVersionUID = 1L;

    public BugTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    public BugTableDisplay(TableViewField requiredColumn, List<TableViewField> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    public BugTableDisplay(String viewId, TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(BugService.class), SimpleBug.class, viewId, requiredColumn, displayColumns);

        this.addGeneratedColumn("assignuserFullName", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            return new ProjectUserLink(bug.getAssignuser(), bug.getAssignUserAvatarId(), bug.getAssignuserFullName());
        });

        this.addGeneratedColumn("loguserFullName", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            return new ProjectUserLink(bug.getLogby(), bug.getLoguserAvatarId(), bug.getLoguserFullName());
        });

        this.addGeneratedColumn("summary", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            LabelLink b = new LabelLink(bug.getSummary(), ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(), bug.getProjectShortName()));

            if (StringUtils.isNotBlank(bug.getPriority())) {
                b.setIconLink(ProjectAssetsManager.getBugPriority(bug.getPriority()));
                b.addStyleName("bug-" + bug.getPriority().toLowerCase());
            }

            b.setDescription(ProjectTooltipGenerator.generateToolTipBug(AppContext.getUserLocale(), AppContext.getDateFormat(),
                    bug, AppContext.getSiteUrl(), AppContext.getUserTimeZone(), false));

            if (bug.isCompleted()) {
                b.addStyleName(UIConstants.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                b.addStyleName(UIConstants.LINK_OVERDUE);
            }
            return b;
        });

        this.addGeneratedColumn("severity", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            Label lbPriority = new Label(AppContext.getMessage(BugSeverity.class, bug.getSeverity()));
            lbPriority.setIcon(FontAwesome.STAR);
            if (bug.getSeverity() != null) {
                lbPriority.addStyleName("bug-severity-" + bug.getSeverity().toLowerCase());
            }
            return lbPriority;
        });

        this.addGeneratedColumn("duedate", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            return new ELabel().prettyDate(bug.getDuedate());
        });

        this.addGeneratedColumn("createdtime", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            return new ELabel().prettyDateTime(bug.getCreatedtime());
        });

        this.addGeneratedColumn("resolution", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            return new Label(AppContext.getMessage(BugResolution.class, bug.getResolution()));
        });
        this.setWidth("100%");
    }
}
