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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.parameters.*;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ProjectPagedList extends DefaultBeanPagedList<ProjectService, ProjectSearchCriteria, SimpleProject> {
    private static final long serialVersionUID = 1L;

    public ProjectPagedList() {
        super(ApplicationContextUtil.getSpringBean(ProjectService.class), new ProjectRowDisplayHandler(), 4);
    }

    public static class ProjectRowDisplayHandler implements
            AbstractBeanPagedList.RowDisplayHandler<SimpleProject> {

        @Override
        public Component generateRow(final SimpleProject project,
                                     final int rowIndex) {
            final CssLayout layout = new CssLayout();
            layout.setWidth("100%");
            layout.setStyleName("projectblock");

            final HorizontalLayout projectLayout = new HorizontalLayout();
            projectLayout.setWidth("100%");
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
            final LabelLink projectLink = new LabelLink(project.getName(),
                    ProjectLinkBuilder.generateProjectFullLink(project.getId()));
            projectLink.addStyleName("project-name");
            linkIconFix.addComponent(projectLink);
            linkIconFix.setExpandRatio(projectLink, 1.0f);

            ButtonLink projectMember = new ButtonLink(
                    project.getNumActiveMembers() + " member"
                            + (project.getNumActiveMembers() > 1 ? "s" : ""),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = -7865685578305013464L;

                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            EventBusFactory
                                    .getInstance()
                                    .post(new ProjectEvent.GotoMyProject(
                                            this,
                                            new PageActionChain(
                                                    new ProjectScreenData.Goto(
                                                            project.getId()),
                                                    new ProjectMemberScreenData.Search(
                                                            null))));
                        }
                    }, false);
            projectMember.addStyleName("member-count-lbl");
            HorizontalLayout metaInfo = new HorizontalLayout();
            metaInfo.setDefaultComponentAlignment(Alignment.TOP_LEFT);
            metaInfo.setWidth("100%");
            metaInfo.setSpacing(true);
            metaInfo.addComponent(projectMember);
            Label createdTimeLbl = new Label(" - "
                    + AppContext.getMessage(ProjectI18nEnum.OPT_CREATED_ON,
                    AppContext.formatDate(project.getCreatedtime())));
            createdTimeLbl.setStyleName("createdtime-lbl");
            createdTimeLbl.setSizeUndefined();
            metaInfo.addComponent(createdTimeLbl);
            metaInfo.setExpandRatio(createdTimeLbl, 1.0f);
            linkIconFix.addComponent(metaInfo);

            projectLink.setWidth("100%");
            linkWrapper.addComponent(linkIconFix);
            projectLayout.addComponent(linkWrapper);

            final MVerticalLayout projectStatusLayout = new MVerticalLayout().withWidth("180px");
            projectStatusLayout
                    .setDefaultComponentAlignment(Alignment.TOP_CENTER);

            final VerticalLayout taskStatus = new VerticalLayout();
            taskStatus.setWidth("100%");
            taskStatus.setSpacing(true);
            HorizontalLayout taskLblWrap = new HorizontalLayout();
            taskLblWrap.setWidth("100%");
            Label taskStatusLbl = new Label("Tasks");
            taskStatusLbl.setStyleName("status-lbl");
            taskLblWrap.addComponent(taskStatusLbl);

            final ButtonLink taskStatusBtn = new ButtonLink(
                    project.getNumOpenTasks() + "/" + project.getNumTasks(),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            EventBusFactory
                                    .getInstance()
                                    .post(new ProjectEvent.GotoMyProject(
                                            this,
                                            new PageActionChain(
                                                    new ProjectScreenData.Goto(
                                                            project.getId()),
                                                    new TaskGroupScreenData.GotoDashboard())));
                        }
                    }, false);
            taskLblWrap.addComponent(taskStatusBtn);
            taskLblWrap.setComponentAlignment(taskStatusBtn,
                    Alignment.TOP_RIGHT);
            taskStatus.addComponent(taskLblWrap);
            float taskValue = (project.getNumTasks() != 0) ? ((float) (project
                    .getNumTasks() - project.getNumOpenTasks()) / project
                    .getNumTasks()) : 1;
            ProgressBar taskProgressBar = new ProgressBar(taskValue);
            taskProgressBar.setStyleName("medium");
            taskStatus.addComponent(taskProgressBar);
            taskStatus.setComponentAlignment(taskProgressBar,
                    Alignment.TOP_LEFT);
            projectStatusLayout.addComponent(taskStatus);

            final VerticalLayout bugStatus = new VerticalLayout();
            bugStatus.setWidth("100%");
            bugStatus.setSpacing(true);
            HorizontalLayout bugLblWrap = new HorizontalLayout();
            bugLblWrap.setWidth("100%");
            Label bugLbl = new Label("Bugs");
            bugLbl.setStyleName("status-lbl");
            bugLblWrap.addComponent(bugLbl);
            final ButtonLink bugStatusBtn = new ButtonLink(
                    project.getNumOpenBugs() + "/" + project.getNumBugs(),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            EventBusFactory
                                    .getInstance()
                                    .post(new ProjectEvent.GotoMyProject(
                                            this,
                                            new PageActionChain(
                                                    new ProjectScreenData.Goto(
                                                            project.getId()),
                                                    new BugScreenData.GotoDashboard())));
                        }
                    }, false);
            bugLblWrap.addComponent(bugStatusBtn);
            bugLblWrap.setComponentAlignment(bugStatusBtn, Alignment.TOP_RIGHT);
            bugStatus.addComponent(bugLblWrap);
            float bugValue = (project.getNumBugs() != 0) ? ((float) (project
                    .getNumBugs() - project.getNumOpenBugs()) / project
                    .getNumBugs()) : 1;
            ProgressBar bugProgressBar = new ProgressBar(bugValue);
            bugProgressBar.setStyleName("medium");
            bugStatus.addComponent(bugProgressBar);
            bugStatus.setComponentAlignment(bugProgressBar, Alignment.TOP_LEFT);
            projectStatusLayout.addComponent(bugStatus);

            MVerticalLayout phaseStatusLayout = new MVerticalLayout().withMargin(false).withWidth("100%").withStyleName("phase-status-layout");


            Label phaseLbl = new Label("Phases");
            phaseLbl.setStyleName("status-lbl");
            phaseLbl.addStyleName("phase-status-lbl");
            phaseLbl.setSizeUndefined();
            phaseStatusLayout.addComponent(phaseLbl);

            HorizontalLayout phaseStatus = new HorizontalLayout();
            phaseStatus.setWidth("100%");
            phaseStatus.setDefaultComponentAlignment(Alignment.TOP_CENTER);
            Button.ClickListener goToPhaseListener = new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory
                            .getInstance()
                            .post(new ProjectEvent.GotoMyProject(
                                    this,
                                    new PageActionChain(
                                            new ProjectScreenData.Goto(project
                                                    .getId()),
                                            new MilestoneScreenData.Search(null))));
                }
            };
            Button closePhaseBtn = new Button(String.format(
                    "%d <small>%s</small>", project.getNumClosedPhase(),
                    AppContext.getMessage(OptionI18nEnum.MilestoneStatus.Closed)),
                    goToPhaseListener);
            closePhaseBtn.setHtmlContentAllowed(true);
            closePhaseBtn.setStyleName("phase-status-btn");
            phaseStatus.addComponent(closePhaseBtn);

            Button inProgressPhaseBtn = new Button(String.format(
                    "%d <small>%s</small>", project.getNumInProgressPhase(),
                    AppContext.getMessage(OptionI18nEnum.MilestoneStatus.InProgress)),
                    goToPhaseListener);
            inProgressPhaseBtn.setHtmlContentAllowed(true);
            inProgressPhaseBtn.setStyleName("phase-status-btn");
            phaseStatus.addComponent(inProgressPhaseBtn);

            Button futurePhaseBtn = new Button(String.format(
                    "%d <small>%s</small>", project.getNumFuturePhase(),
                    AppContext.getMessage(OptionI18nEnum.MilestoneStatus.Future)),
                    goToPhaseListener);
            futurePhaseBtn.setHtmlContentAllowed(true);
            futurePhaseBtn.setStyleName("phase-status-btn");
            phaseStatus.addComponent(futurePhaseBtn);

            phaseStatusLayout.addComponent(phaseStatus);

            linkIconFix.addComponent(phaseStatusLayout);

            projectLayout.addComponent(projectStatusLayout);

            projectLayout.setExpandRatio(linkWrapper, 1.0f);

            layout.addComponent(projectLayout);
            return layout;
        }
    }
}
