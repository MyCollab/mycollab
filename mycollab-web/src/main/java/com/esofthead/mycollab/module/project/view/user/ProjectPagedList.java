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
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ProjectPagedList extends DefaultBeanPagedList<ProjectService, ProjectSearchCriteria, SimpleProject> {
    private static final long serialVersionUID = 1L;

    public ProjectPagedList() {
        super(ApplicationContextUtil.getSpringBean(ProjectService.class), new ProjectRowDisplayHandler(), 4);
    }

    public static class ProjectRowDisplayHandler implements AbstractBeanPagedList.RowDisplayHandler<SimpleProject> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, final SimpleProject project, final int rowIndex) {
            final CssLayout layout = new CssLayout();
            layout.setWidth("100%");
            layout.addStyleName("projectblock");

            final MHorizontalLayout projectLayout = new MHorizontalLayout().withSpacing(false).withWidth("100%");
            projectLayout.addStyleName("project-status");

            final CssLayout linkWrapper = new CssLayout();
            linkWrapper.setWidth("100%");

            if (project.isArchived()) {
                linkWrapper.addStyleName("projectlink-wrapper-archived");
            } else {
                linkWrapper.addStyleName("projectlink-wrapper");
            }

            final VerticalLayout linkIconFix = new VerticalLayout();
            linkIconFix.setWidth("100%");
            MCssLayout prjHeaderLayout = new MCssLayout().withFullWidth();

            final LabelLink projectLbl = new LabelLink(String.format("[%s] %s", project.getShortname(), project.getName()),
                    ProjectLinkBuilder.generateProjectFullLink(project.getId()));
            projectLbl.addStyleName(ValoTheme.LABEL_BOLD);
            projectLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
            projectLbl.setWidthUndefined();

            prjHeaderLayout.addComponent(projectLbl);

            linkIconFix.addComponent(prjHeaderLayout);

            Label projectMemberBtn = new ELabel(FontAwesome.USERS.getHtml() + " " + project.getNumActiveMembers(),
                    ContentMode.HTML).withDescription("Active members").withStyleName(UIConstants.LABEL_META_INFO);
            MHorizontalLayout metaInfo = new MHorizontalLayout();
            metaInfo.setDefaultComponentAlignment(Alignment.TOP_LEFT);
            metaInfo.addComponent(projectMemberBtn);
            Label createdTimeLbl = new ELabel(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime(project.getCreatedtime()),
                    ContentMode.HTML).withDescription("Created time").withStyleName(UIConstants.LABEL_META_INFO);
            metaInfo.addComponent(createdTimeLbl);

            Label billableHoursLbl = new ELabel(FontAwesome.MONEY.getHtml() + " " + NumberUtils.roundDouble(2, project.getTotalBillableHours()),
                    ContentMode.HTML).withDescription("Billable hours").withStyleName(UIConstants.LABEL_META_INFO);
            metaInfo.addComponent(billableHoursLbl);

            Label nonBillableHoursLbl = new ELabel(FontAwesome.GIFT.getHtml() + " " + NumberUtils.roundDouble(2,
                    project.getTotalNonBillableHours()), ContentMode.HTML)
                    .withDescription("Non billable hours").withStyleName(UIConstants.LABEL_META_INFO);
            metaInfo.addComponent(nonBillableHoursLbl);

            linkIconFix.addComponent(metaInfo);

            projectLbl.setWidth("100%");
            linkWrapper.addComponent(linkIconFix);
            projectLayout.addComponent(linkWrapper);

            final MVerticalLayout projectStatusLayout = new MVerticalLayout().withWidth("180px").withFullHeight();
            projectStatusLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            final VerticalLayout taskStatus = new VerticalLayout();
            taskStatus.setWidth("100%");
            taskStatus.setSpacing(true);
            HorizontalLayout taskLblWrap = new HorizontalLayout();
            taskLblWrap.setWidth("100%");
            ELabel taskStatusLbl = new ELabel("Tasks").withStyleName(UIConstants.LABEL_META_INFO);
            taskLblWrap.addComponent(taskStatusLbl);

            final ELabel taskStatusBtn = new ELabel((project.getNumTasks() - project.getNumOpenTasks()) + "/" +
                    project.getNumTasks()).withStyleName(UIConstants.LABEL_META_INFO);
            taskStatusBtn.setWidthUndefined();
            taskLblWrap.addComponent(taskStatusBtn);
            taskLblWrap.setComponentAlignment(taskStatusBtn, Alignment.TOP_RIGHT);
            taskStatus.addComponent(taskLblWrap);
            float taskValue = (project.getNumTasks() != 0) ? ((float) (project.getNumTasks() - project.getNumOpenTasks()) / project
                    .getNumTasks()) : 0;
            ProgressBar taskProgressBar = new ProgressBar(taskValue);
            taskProgressBar.setStyleName("medium");
            taskStatus.addComponent(taskProgressBar);
            taskStatus.setComponentAlignment(taskProgressBar, Alignment.TOP_LEFT);
            projectStatusLayout.addComponent(taskStatus);

            final VerticalLayout bugStatus = new VerticalLayout();
            bugStatus.setWidth("100%");
            bugStatus.setSpacing(true);
            HorizontalLayout bugLblWrap = new HorizontalLayout();
            bugLblWrap.setWidth("100%");
            ELabel bugLbl = new ELabel("Bugs").withStyleName(UIConstants.LABEL_META_INFO);
            bugLblWrap.addComponent(bugLbl);
            ELabel bugStatusBtn = new ELabel((project.getNumBugs() - project.getNumOpenBugs()) + "/" + project.getNumBugs()).withStyleName(UIConstants.LABEL_META_INFO);
            bugStatusBtn.setWidthUndefined();
            bugLblWrap.addComponent(bugStatusBtn);
            bugLblWrap.setComponentAlignment(bugStatusBtn, Alignment.TOP_RIGHT);
            bugStatus.addComponent(bugLblWrap);
            float bugValue = (project.getNumBugs() != 0) ? ((float) (project.getNumBugs() - project.getNumOpenBugs()) / project.getNumBugs()) : 0;
            ProgressBar bugProgressBar = new ProgressBar(bugValue);
            bugProgressBar.setStyleName(UIConstants.PROGRESS_BAR_MEDIUM);
            bugStatus.addComponent(bugProgressBar);
            bugStatus.setComponentAlignment(bugProgressBar, Alignment.TOP_LEFT);
            projectStatusLayout.addComponent(bugStatus);

            MVerticalLayout phaseStatusLayout = new MVerticalLayout().withMargin(false).withWidth("100%");

            Label phaseLbl = new Label("Phases");
            phaseLbl.setStyleName(UIConstants.LABEL_META_INFO);
            phaseLbl.setSizeUndefined();
            phaseStatusLayout.addComponent(phaseLbl);

            HorizontalLayout phaseStatus = new HorizontalLayout();
            phaseStatus.setWidth("100%");
            phaseStatus.setDefaultComponentAlignment(Alignment.TOP_CENTER);

            Button closePhaseBtn = new Button(String.format("%d <small>%s</small>", project.getNumClosedPhase(),
                    AppContext.getMessage(OptionI18nEnum.MilestoneStatus.Closed)));
            closePhaseBtn.addStyleName(UIConstants.BUTTON_BLOCK);
            closePhaseBtn.addStyleName(ValoTheme.BUTTON_SMALL);
            closePhaseBtn.setHtmlContentAllowed(true);
            phaseStatus.addComponent(closePhaseBtn);

            Button inProgressPhaseBtn = new Button(String.format(
                    "%d <small>%s</small>", project.getNumInProgressPhase(),
                    AppContext.getMessage(OptionI18nEnum.MilestoneStatus.InProgress)));
            inProgressPhaseBtn.setHtmlContentAllowed(true);
            inProgressPhaseBtn.addStyleName(UIConstants.BUTTON_BLOCK);
            inProgressPhaseBtn.addStyleName(ValoTheme.BUTTON_SMALL);
            phaseStatus.addComponent(inProgressPhaseBtn);

            Button futurePhaseBtn = new Button(String.format(
                    "%d <small>%s</small>", project.getNumFuturePhase(),
                    AppContext.getMessage(OptionI18nEnum.MilestoneStatus.Future)));
            futurePhaseBtn.setHtmlContentAllowed(true);
            futurePhaseBtn.addStyleName(UIConstants.BUTTON_BLOCK);
            futurePhaseBtn.addStyleName(ValoTheme.BUTTON_SMALL);
            phaseStatus.addComponent(futurePhaseBtn);

            phaseStatusLayout.addComponent(phaseStatus);
            linkIconFix.addComponent(phaseStatusLayout);
            projectLayout.with(projectStatusLayout).expand(linkWrapper);
            layout.addComponent(projectLayout);
            return layout;
        }
    }
}
