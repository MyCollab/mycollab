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
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BugTableDisplay extends DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> {
    private static final long serialVersionUID = 1L;

    BugTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    BugTableDisplay(TableViewField requiredColumn, List<TableViewField> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    BugTableDisplay(String viewId, TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(BugService.class), SimpleBug.class, viewId, requiredColumn, displayColumns);

        this.addGeneratedColumn("assignuserFullName", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            return new ProjectUserLink(bug.getProjectid(), bug.getAssignuser(), bug.getAssignUserAvatarId(), bug.getAssignuserFullName());
        });

        this.addGeneratedColumn("loguserFullName", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            return new ProjectUserLink(bug.getProjectid(), bug.getCreateduser(), bug.getLoguserAvatarId(), bug.getLoguserFullName());
        });

        this.addGeneratedColumn("name", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            LabelLink b = new LabelLink(bug.getName(), ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(),
                    bug.getProjectShortName()));

            if (StringUtils.isNotBlank(bug.getPriority())) {
                b.setIconLink(ProjectAssetsManager.getPriority(bug.getPriority()));
                b.addStyleName("priority-" + bug.getPriority().toLowerCase());
            }

            b.setDescription(ProjectTooltipGenerator.generateToolTipBug(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                    bug, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone(), false));

            if (bug.isCompleted()) {
                b.addStyleName(WebThemes.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                b.addStyleName(WebThemes.LINK_OVERDUE);
            }
            return b;
        });

        this.addGeneratedColumn("severity", (source, itemId, columnId) -> {
            SimpleBug bug = getBeanByIndex(itemId);
            Label lbPriority = new Label(UserUIContext.getMessage(BugSeverity.class, bug.getSeverity()));
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
            return new Label(UserUIContext.getMessage(BugResolution.class, bug.getResolution()));
        });
        this.setWidth("100%");
    }
}
