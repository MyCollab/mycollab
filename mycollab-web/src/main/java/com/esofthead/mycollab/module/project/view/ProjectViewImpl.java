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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.VariableInjecter;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.*;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.bug.BugPresenter;
import com.esofthead.mycollab.module.project.view.file.FilePresenter;
import com.esofthead.mycollab.module.project.view.message.MessagePresenter;
import com.esofthead.mycollab.module.project.view.milestone.MilestonePresenter;
import com.esofthead.mycollab.module.project.view.page.PagePresenter;
import com.esofthead.mycollab.module.project.view.parameters.*;
import com.esofthead.mycollab.module.project.view.problem.IProblemPresenter;
import com.esofthead.mycollab.module.project.view.risk.IRiskPresenter;
import com.esofthead.mycollab.module.project.view.settings.UserSettingPresenter;
import com.esofthead.mycollab.module.project.view.standup.IStandupPresenter;
import com.esofthead.mycollab.module.project.view.task.TaskPresenter;
import com.esofthead.mycollab.module.project.view.time.ITimeTrackingPresenter;
import com.esofthead.mycollab.module.project.view.user.ProjectDashboardPresenter;
import com.esofthead.mycollab.module.project.view.user.ProjectInfoComponent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet.TabImpl;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
@ViewComponent
public class ProjectViewImpl extends AbstractPageView implements ProjectView {
    private ProjectViewWrap viewWrap;

    @Override
    public void initView(final SimpleProject project) {
        this.setSizeFull();
        this.removeAllComponents();
        viewWrap = new ProjectViewWrap(project);
        ControllerRegistry.addController(new ProjectController(this));
        ProjectInfoComponent infoComp = new ProjectInfoComponent(project);
        this.with(infoComp, viewWrap).expand(viewWrap);
    }

    @Override
    public void setNavigatorVisibility(boolean visibility) {
        viewWrap.setNavigatorVisibility(visibility);
    }

    @Override
    public Component gotoSubView(String viewId) {
        return viewWrap.gotoSubView(viewId);
    }

    @Override
    public void updateProjectFeatures() {
        viewWrap.buildComponents();
    }

    private class ProjectViewWrap extends AbstractCssPageView {
        private ProjectVerticalTabsheet myProjectTab;

        private ProjectDashboardPresenter dashboardPresenter;
        private MessagePresenter messagePresenter;
        private MilestonePresenter milestonesPresenter;
        private TaskPresenter taskPresenter;
        private BugPresenter bugPresenter;
        private PagePresenter pagePresenter;
        private FilePresenter filePresenter;
        private IProblemPresenter problemPresenter;
        private IRiskPresenter riskPresenter;
        private ITimeTrackingPresenter timePresenter;
        private UserSettingPresenter userPresenter;
        private IStandupPresenter standupPresenter;

        ProjectViewWrap(final SimpleProject project) {
            super();
            this.setWidth("100%");

            this.addStyleName("main-content-wrapper");
            this.addStyleName("projectDashboardView");

            myProjectTab = new ProjectVerticalTabsheet();
            myProjectTab.setSizeFull();
            myProjectTab.setNavigatorWidth("100%");
            myProjectTab.setNavigatorStyleName("sidebar-menu");
            myProjectTab.setContainerStyleName("tab-content");

            myProjectTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
                @Override
                public void selectedTabChange(SelectedTabChangeEvent event) {
                    Tab tab = ((ProjectVerticalTabsheet) event.getSource()).getSelectedTab();
                    String caption = ((TabImpl) tab).getTabId();
                    if (ProjectTypeConstants.MESSAGE.equals(caption)) {
                        messagePresenter.go(ProjectViewImpl.this, null);
                    } else if (ProjectTypeConstants.MILESTONE.equals(caption)) {
                        milestonesPresenter.go(ProjectViewImpl.this, new MilestoneScreenData.Roadmap());
                    } else if (ProjectTypeConstants.TASK.equals(caption)) {
                        taskPresenter.go(ProjectViewImpl.this, null);
                    } else if (ProjectTypeConstants.BUG.equals(caption)) {
                        bugPresenter.go(ProjectViewImpl.this, null);
                    } else if (ProjectTypeConstants.RISK.equals(caption)) {
                        RiskSearchCriteria searchCriteria = new RiskSearchCriteria();
                        searchCriteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));
                        riskPresenter.go(ProjectViewImpl.this, new RiskScreenData.Search(searchCriteria));
                    } else if (ProjectTypeConstants.FILE.equals(caption)) {
                        filePresenter.go(ProjectViewImpl.this, new FileScreenData.GotoDashboard());
                    } else if (ProjectTypeConstants.PAGE.equals(caption)) {
                        pagePresenter.go(ProjectViewImpl.this,
                                new PageScreenData.Search(CurrentProjectVariables.getBasePagePath()));
                    } else if (ProjectTypeConstants.PROBLEM.equals(caption)) {
                        ProblemSearchCriteria searchCriteria = new ProblemSearchCriteria();
                        searchCriteria.setProjectId(new NumberSearchField(
                                SearchField.AND, CurrentProjectVariables.getProjectId()));
                        problemPresenter.go(ProjectViewImpl.this, new ProblemScreenData.Search(searchCriteria));
                    } else if (ProjectTypeConstants.DASHBOARD.equals(caption)) {
                        dashboardPresenter.go(ProjectViewImpl.this, null);
                    } else if (ProjectTypeConstants.MEMBER.equals(caption)) {
                        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE));
                        userPresenter.go(ProjectViewImpl.this, new ProjectMemberScreenData.Search(criteria));
                    } else if (ProjectTypeConstants.TIME.equals(caption)) {
                        ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
                        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                        searchCriteria.addExtraField(DateParam.inRangeDate(ItemTimeLoggingSearchCriteria.p_logDates,
                                VariableInjecter.THIS_WEEK));
                        timePresenter.go(ProjectViewImpl.this, new TimeTrackingScreenData.Search(searchCriteria));
                    } else if (ProjectTypeConstants.STANDUP.equals(caption)) {
                        StandupReportSearchCriteria criteria = new StandupReportSearchCriteria();
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setOnDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS(), DateSearchField.EQUAL));
                        standupPresenter.go(ProjectViewImpl.this, new StandupScreenData.Search(criteria));
                    }
                }
            });

            VerticalLayout contentWrapper = myProjectTab.getContentWrapper();
            contentWrapper.addStyleName("main-content");
            MHorizontalLayout topPanel = new MHorizontalLayout().withMargin(true).withWidth("100%").withStyleName
                    ("top-panel").withHeight("42px");
            contentWrapper.addComponentAsFirst(topPanel);

            CssLayout navigatorWrapper = myProjectTab.getNavigatorWrapper();
            navigatorWrapper.setWidth("250px");

            buildComponents();
            this.addComponent(myProjectTab);

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.setProject(project);

            topPanel.with(breadCrumb).withAlign(breadCrumb, Alignment.MIDDLE_LEFT).expand(breadCrumb);

            if (project.getContextask() == null || project.getContextask()) {
                ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
                searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                searchCriteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE));
                ProjectMemberService prjMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
                int totalMembers = prjMemberService.getTotalCount(searchCriteria);
                if (totalMembers < 2) {
                    UI.getCurrent().addWindow(new AskToAddMoreMembersWindow());
                }
            }
        }

        void setNavigatorVisibility(boolean visibility) {
            myProjectTab.setNavigatorVisibility(visibility);
        }

        Component gotoSubView(String viewId) {
            return myProjectTab.selectTab(viewId);
        }

        private void buildComponents() {
            Integer prjId = CurrentProjectVariables.getProjectId();

            myProjectTab.addTab(constructProjectDashboardComponent(), ProjectTypeConstants.DASHBOARD, 1,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD),
                    GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectLink(prjId));

            if (CurrentProjectVariables.hasMessageFeature()) {
                myProjectTab.addTab(constructProjectMessageComponent(), ProjectTypeConstants.MESSAGE, 2,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateMessagesLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.MESSAGE);
            }

            if (CurrentProjectVariables.hasPhaseFeature()) {
                myProjectTab.addTab(constructProjectMilestoneComponent(), ProjectTypeConstants.MILESTONE, 3,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateMilestonesLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.MILESTONE);
            }

            if (CurrentProjectVariables.hasTaskFeature()) {
                myProjectTab.addTab(constructTaskDashboardComponent(),
                        ProjectTypeConstants.TASK, 4, AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TASK),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateTaskDashboardLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.TASK);
            }

            if (CurrentProjectVariables.hasBugFeature()) {
                myProjectTab.addTab(constructProjectBugComponent(), ProjectTypeConstants.BUG, 5,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_BUG),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.BUG);
            }

            if (CurrentProjectVariables.hasPageFeature()) {
                myProjectTab.addTab(constructProjectPageComponent(), ProjectTypeConstants.PAGE, 6,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_PAGE),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.PAGE);
            }

            if (CurrentProjectVariables.hasFileFeature()) {
                myProjectTab.addTab(constructProjectFileComponent(), ProjectTypeConstants.FILE, 7,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateFileDashboardLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.FILE);
            }

            if (CurrentProjectVariables.hasRiskFeature()) {
                myProjectTab.addTab(constructProjectRiskComponent(), ProjectTypeConstants.RISK, 8,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_RISK),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateRisksLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.RISK);
            }

            if (CurrentProjectVariables.hasProblemFeature()) {
                myProjectTab.addTab(constructProjectProblemComponent(), ProjectTypeConstants.PROBLEM, 9,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_PROBLEM),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateProblemsLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.PROBLEM);
            }

            if (CurrentProjectVariables.hasTimeFeature()) {
                myProjectTab.addTab(constructTimeTrackingComponent(), ProjectTypeConstants.TIME, 10,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateTimeReportLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.TIME);
            }

            if (CurrentProjectVariables.hasStandupFeature()) {
                myProjectTab.addTab(constructProjectStandupMeeting(), ProjectTypeConstants.STANDUP, 11,
                        AppContext.getMessage(ProjectCommonI18nEnum.VIEW_STANDAUP),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateStandupDashboardLink(prjId));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.STANDUP);
            }

            myProjectTab.addTab(constructProjectUsers(), ProjectTypeConstants.MEMBER, 13,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MEMBER),
                    GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateUsersLink(prjId));

            myProjectTab.addToogleNavigatorControl();
        }

        private Component constructProjectDashboardComponent() {
            dashboardPresenter = PresenterResolver.getPresenter(ProjectDashboardPresenter.class);
            return dashboardPresenter.getView();
        }

        private Component constructProjectUsers() {
            userPresenter = PresenterResolver.getPresenter(UserSettingPresenter.class);
            return userPresenter.getView();
        }

        private Component constructProjectMessageComponent() {
            messagePresenter = PresenterResolver.getPresenter(MessagePresenter.class);
            return messagePresenter.getView();
        }

        private Component constructProjectPageComponent() {
            pagePresenter = PresenterResolver.getPresenter(PagePresenter.class);
            return pagePresenter.getView();
        }

        private Component constructProjectMilestoneComponent() {
            milestonesPresenter = PresenterResolver.getPresenter(MilestonePresenter.class);
            return milestonesPresenter.getView();
        }

        private Component constructProjectRiskComponent() {
            riskPresenter = PresenterResolver.getPresenter(IRiskPresenter.class);
            return riskPresenter.getView();
        }

        private Component constructProjectProblemComponent() {
            problemPresenter = PresenterResolver.getPresenter(IProblemPresenter.class);
            return problemPresenter.getView();
        }

        private Component constructTimeTrackingComponent() {
            timePresenter = PresenterResolver.getPresenter(ITimeTrackingPresenter.class);
            return timePresenter.getView();
        }

        private Component constructProjectStandupMeeting() {
            standupPresenter = PresenterResolver.getPresenter(IStandupPresenter.class);
            return standupPresenter.getView();
        }

        private Component constructTaskDashboardComponent() {
            taskPresenter = PresenterResolver.getPresenter(TaskPresenter.class);
            return taskPresenter.getView();
        }

        private Component constructProjectBugComponent() {
            bugPresenter = PresenterResolver.getPresenter(BugPresenter.class);
            return bugPresenter.getView();
        }

        private Component constructProjectFileComponent() {
            filePresenter = PresenterResolver.getPresenter(FilePresenter.class);
            return filePresenter.getView();
        }
    }

    private static class AskToAddMoreMembersWindow extends Window {
        AskToAddMoreMembersWindow() {
            super("Question");
            this.setWidth("600px");
            this.setResizable(false);
            this.setModal(true);

            MVerticalLayout content = new MVerticalLayout();
            this.setContent(content);

            content.with(new Label("There is very few users in your project. Do you want to invite additional members?"));
            MHorizontalLayout btnControls = new MHorizontalLayout();
            Button skipBtn = new Button("Skip", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
                    SimpleProject project = CurrentProjectVariables.getProject();
                    project.setContextask(false);
                    projectService.updateSelectiveWithSession(project, AppContext.getUsername());
                    AskToAddMoreMembersWindow.this.close();
                }
            });
            skipBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

            Button addNewMembersBtn = new Button("Add Members", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    AskToAddMoreMembersWindow.this.close();
                    EventBusFactory.getInstance().post(
                            new ProjectMemberEvent.GotoInviteMembers(this, null));
                }
            });
            addNewMembersBtn.setStyleName(UIConstants.BUTTON_ACTION);
            btnControls.with(skipBtn, addNewMembersBtn);
            content.with(btnControls).withAlign(btnControls, Alignment.MIDDLE_RIGHT);
        }
    }
}
