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
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.*;
import com.esofthead.mycollab.module.project.events.*;
import com.esofthead.mycollab.module.project.i18n.*;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.bug.TrackerPresenter;
import com.esofthead.mycollab.module.project.view.file.IFilePresenter;
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
import com.esofthead.mycollab.module.project.view.user.ProjectListComponent;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.SearchTextField;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet.TabImpl;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
@ViewComponent
public class ProjectViewImpl extends AbstractCssPageView implements ProjectView {
    private ProjectVerticalTabsheet myProjectTab;
    private ProjectDashboardPresenter dashboardPresenter;
    private MessagePresenter messagePresenter;
    private MilestonePresenter milestonesPresenter;
    private TaskPresenter taskPresenter;
    private TrackerPresenter trackerPresenter;
    private PagePresenter pagePresenter;
    private IFilePresenter filePresenter;
    private IProblemPresenter problemPresenter;
    private IRiskPresenter riskPresenter;
    private ITimeTrackingPresenter timePresenter;
    private UserSettingPresenter userPresenter;
    private IStandupPresenter standupPresenter;

    public ProjectViewImpl() {
        super(true);
    }

    @Override
    public void initView(final SimpleProject project) {
        this.removeAllComponents();
        updateVerticalTabsheetFixStatus();
        ControllerRegistry.addController(new ProjectController(this));
        this.setWidth("100%");

        this.addStyleName("main-content-wrapper");
        this.addStyleName("projectDashboardView");

        myProjectTab = new ProjectVerticalTabsheet();
        myProjectTab.setSizeFull();
        myProjectTab.setNavigatorWidth("100%");
        myProjectTab.setNavigatorStyleName("sidebar-menu");
        myProjectTab.setContainerStyleName("tab-content");

        myProjectTab
                .addSelectedTabChangeListener(new SelectedTabChangeListener() {

                    @Override
                    public void selectedTabChange(SelectedTabChangeEvent event) {
                        Tab tab = ((ProjectVerticalTabsheet) event.getSource())
                                .getSelectedTab();
                        String caption = ((TabImpl) tab).getTabId();
                        if (ProjectTypeConstants.MESSAGE.equals(caption)) {
                            messagePresenter.go(ProjectViewImpl.this, null);
                        } else if (ProjectTypeConstants.MILESTONE.equals(caption)) {
                            MilestoneSearchCriteria searchCriteria = new MilestoneSearchCriteria();
                            searchCriteria.setProjectId(new NumberSearchField(
                                    SearchField.AND, CurrentProjectVariables
                                    .getProjectId()));
                            gotoMilestoneView(new MilestoneScreenData.Search(
                                    searchCriteria));
                        } else if (ProjectTypeConstants.TASK.equals(caption)) {
                            taskPresenter.go(ProjectViewImpl.this, null);
                        } else if (ProjectTypeConstants.BUG.equals(caption)) {
                            gotoBugView(null);
                        } else if (ProjectTypeConstants.RISK.equals(caption)) {
                            RiskSearchCriteria searchCriteria = new RiskSearchCriteria();
                            searchCriteria.setProjectId(new NumberSearchField(
                                    SearchField.AND, CurrentProjectVariables
                                    .getProjectId()));
                            gotoRiskView(new RiskScreenData.Search(
                                    searchCriteria));
                        } else if (ProjectTypeConstants.FILE.equals(caption)) {
                            filePresenter.go(ProjectViewImpl.this,
                                    new FileScreenData.GotoDashboard());
                        } else if (ProjectTypeConstants.PAGE.equals(caption)) {
                            pagePresenter.go(
                                    ProjectViewImpl.this,
                                    new PageScreenData.Search(
                                            CurrentProjectVariables
                                                    .getBasePagePath()));
                        } else if (ProjectTypeConstants.PROBLEM.equals(caption)) {
                            ProblemSearchCriteria searchCriteria = new ProblemSearchCriteria();
                            searchCriteria.setProjectId(new NumberSearchField(
                                    SearchField.AND, CurrentProjectVariables
                                    .getProjectId()));
                            problemPresenter
                                    .go(ProjectViewImpl.this,
                                            new ProblemScreenData.Search(
                                                    searchCriteria));
                        } else if (ProjectTypeConstants.DASHBOARD.equals(caption)) {
                            dashboardPresenter.go(ProjectViewImpl.this, null);
                        } else if (ProjectTypeConstants.MEMBER.equals(caption)) {
                            ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                            criteria.setProjectId(new NumberSearchField(
                                    CurrentProjectVariables.getProjectId()));
                            criteria.setStatus(new StringSearchField(
                                    ProjectMemberStatusConstants.ACTIVE));
                            gotoUsersAndGroup(new ProjectMemberScreenData.Search(
                                    criteria));
                        } else if (ProjectTypeConstants.TIME.equals(caption)) {
                            ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
                            searchCriteria
                                    .setProjectIds(new SetSearchField<>(
                                            CurrentProjectVariables
                                                    .getProjectId()));
                            searchCriteria.setRangeDate(ItemTimeLoggingSearchCriteria
                                    .getCurrentRangeDateOfWeekSearchField());
                            gotoTimeTrackingView(new TimeTrackingScreenData.Search(
                                    searchCriteria));
                        } else if (ProjectTypeConstants.STANDUP.equals(caption)) {
                            StandupReportSearchCriteria criteria = new StandupReportSearchCriteria();
                            criteria.setProjectId(new NumberSearchField(
                                    CurrentProjectVariables.getProjectId()));
                            criteria.setOnDate(new DateSearchField(
                                    SearchField.AND, DateSearchField.EQUAL,
                                    new GregorianCalendar().getTime()));
                            standupPresenter.go(ProjectViewImpl.this,
                                    new StandupScreenData.Search(criteria));
                        }

                    }
                });

        VerticalLayout contentWrapper = myProjectTab.getContentWrapper();
        contentWrapper.addStyleName("main-content");
        MHorizontalLayout topPanel = new MHorizontalLayout().withMargin(true).withWidth("100%")
                .withStyleName("top-panel");
        contentWrapper.addComponentAsFirst(topPanel);

        ProjectListComponent prjList = new ProjectListComponent();
        CssLayout navigatorWrapper = myProjectTab.getNavigatorWrapper();
        navigatorWrapper.addComponentAsFirst(prjList);
        navigatorWrapper.setWidth("250px");

        buildComponents();
        this.addComponent(myProjectTab);

        ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadCrumb.setProject(project);

        topPanel.with(breadCrumb).withAlign(breadCrumb, Alignment.MIDDLE_LEFT).expand(breadCrumb);

        if (project.isProjectArchived()) {
            Button activeProjectBtn = new Button(
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.BUTTON_ACTIVE_PROJECT),
                    new ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            ProjectService projectService = ApplicationContextUtil
                                    .getSpringBean(ProjectService.class);
                            project.setProjectstatus(StatusI18nEnum.Open.name());
                            projectService.updateSelectiveWithSession(project,
                                    AppContext.getUsername());

                            PageActionChain chain = new PageActionChain(
                                    new ProjectScreenData.Goto(
                                            CurrentProjectVariables
                                                    .getProjectId()));
                            EventBusFactory.getInstance()
                                    .post(new ProjectEvent.GotoMyProject(this,
                                            chain));

                        }
                    });
            activeProjectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            topPanel.with(activeProjectBtn).withAlign(activeProjectBtn, Alignment.MIDDLE_RIGHT);
        } else {
            SearchTextField searchField = new SearchTextField() {
                public void doSearch(String value) {
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectSearchItemsView(ProjectViewImpl.this, value));
                }
            };

            final PopupButton controlsBtn = new PopupButton();
            controlsBtn.setIcon(FontAwesome.ELLIPSIS_H);
            controlsBtn.addStyleName(UIConstants.THEME_BLANK_LINK);

            MVerticalLayout popupButtonsControl = new MVerticalLayout().withWidth("150px");

            Button createPhaseBtn = new Button(
                    AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(
                                    new MilestoneEvent.GotoAdd(
                                            ProjectViewImpl.this, null));
                        }
                    });
            createPhaseBtn.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.MILESTONES));
            createPhaseBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
            createPhaseBtn.setStyleName("link");
            popupButtonsControl.addComponent(createPhaseBtn);

            Button createTaskBtn = new Button(
                    AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(
                                    new TaskEvent.GotoAdd(ProjectViewImpl.this,
                                            null));
                        }
                    });
            createTaskBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            createTaskBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));
            createTaskBtn.setStyleName("link");
            popupButtonsControl.addComponent(createTaskBtn);

            Button createBugBtn = new Button(
                    AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
                        }
                    });
            createBugBtn.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.BUGS));
            createBugBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
            createBugBtn.setStyleName("link");
            popupButtonsControl.addComponent(createBugBtn);

            Button createRiskBtn = new Button(
                    AppContext.getMessage(RiskI18nEnum.BUTTON_NEW_RISK),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(new RiskEvent.GotoAdd(this, null));
                        }
                    });
            createRiskBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.RISKS));
            createRiskBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));
            createRiskBtn.setStyleName("link");
            popupButtonsControl.addComponent(createRiskBtn);

            Button createProblemBtn = new Button(
                    AppContext.getMessage(ProblemI18nEnum.BUTTON_NEW_PROBLEM),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(
                                    new ProblemEvent.GotoAdd(this, null));
                        }
                    });
            createProblemBtn.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.PROBLEMS));
            createProblemBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROBLEM));
            createProblemBtn.setStyleName("link");
            popupButtonsControl.addComponent(createProblemBtn);

            Button editProjectBtn = new Button(
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.BUTTON_EDIT_PROJECT),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            dashboardPresenter.go(ProjectViewImpl.this,
                                    new ProjectScreenData.Edit(project));
                        }
                    });
            editProjectBtn.setEnabled(CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.PROJECT));
            editProjectBtn.setIcon(FontAwesome.EDIT);
            editProjectBtn.setStyleName("link");
            popupButtonsControl.addComponent(editProjectBtn);

            Button archiveProjectBtn = new Button(
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.BUTTON_ARCHIVE_PROJECT),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            ConfirmDialogExt.show(
                                    UI.getCurrent(),
                                    AppContext
                                            .getMessage(
                                                    GenericI18Enum.WINDOW_WARNING_TITLE,
                                                    SiteConfiguration
                                                            .getSiteName()),
                                    AppContext
                                            .getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_ARCHIVE_MESSAGE),
                                    AppContext
                                            .getMessage(GenericI18Enum.BUTTON_YES),
                                    AppContext
                                            .getMessage(GenericI18Enum.BUTTON_NO),
                                    new ConfirmDialog.Listener() {
                                        private static final long serialVersionUID = 1L;

                                        @Override
                                        public void onClose(ConfirmDialog dialog) {
                                            if (dialog.isConfirmed()) {
                                                ProjectService projectService = ApplicationContextUtil
                                                        .getSpringBean(ProjectService.class);
                                                project.setProjectstatus(StatusI18nEnum.Archived
                                                        .name());
                                                projectService
                                                        .updateSelectiveWithSession(
                                                                project,
                                                                AppContext
                                                                        .getUsername());

                                                PageActionChain chain = new PageActionChain(
                                                        new ProjectScreenData.Goto(
                                                                CurrentProjectVariables
                                                                        .getProjectId()));
                                                EventBusFactory
                                                        .getInstance()
                                                        .post(new ProjectEvent.GotoMyProject(
                                                                this, chain));
                                            }
                                        }
                                    });
                        }
                    });
            archiveProjectBtn.setEnabled(CurrentProjectVariables
                    .canAccess(ProjectRolePermissionCollections.PROJECT));
            archiveProjectBtn.setIcon(FontAwesome.ARCHIVE);
            archiveProjectBtn.setStyleName("link");
            popupButtonsControl.addComponent(archiveProjectBtn);

            if (CurrentProjectVariables
                    .canAccess(ProjectRolePermissionCollections.PROJECT)) {
                Button deleteProjectBtn = new Button(
                        AppContext
                                .getMessage(ProjectCommonI18nEnum.BUTTON_DELETE_PROJECT),
                        new Button.ClickListener() {
                            @Override
                            public void buttonClick(ClickEvent event) {
                                controlsBtn.setPopupVisible(false);
                                ConfirmDialogExt.show(
                                        UI.getCurrent(),
                                        AppContext
                                                .getMessage(
                                                        GenericI18Enum.DIALOG_DELETE_TITLE,
                                                        SiteConfiguration
                                                                .getSiteName()),
                                        AppContext
                                                .getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_DELETE_MESSAGE),
                                        AppContext
                                                .getMessage(GenericI18Enum.BUTTON_YES),
                                        AppContext
                                                .getMessage(GenericI18Enum.BUTTON_NO),
                                        new ConfirmDialog.Listener() {
                                            private static final long serialVersionUID = 1L;

                                            @Override
                                            public void onClose(
                                                    ConfirmDialog dialog) {
                                                if (dialog.isConfirmed()) {
                                                    ProjectService projectService = ApplicationContextUtil
                                                            .getSpringBean(ProjectService.class);
                                                    projectService.removeWithSession(
                                                            CurrentProjectVariables
                                                                    .getProjectId(),
                                                            AppContext
                                                                    .getUsername(),
                                                            AppContext
                                                                    .getAccountId());
                                                    EventBusFactory
                                                            .getInstance()
                                                            .post(new ShellEvent.GotoProjectModule(
                                                                    this, null));
                                                }
                                            }
                                        });
                            }
                        });
                deleteProjectBtn.setEnabled(CurrentProjectVariables
                        .canAccess(ProjectRolePermissionCollections.PROJECT));
                deleteProjectBtn.setIcon(FontAwesome.TRASH_O);
                deleteProjectBtn.setStyleName("link");
                popupButtonsControl.addComponent(deleteProjectBtn);
            }

            controlsBtn.setContent(popupButtonsControl);
            controlsBtn.setWidthUndefined();

            topPanel.with(searchField, controlsBtn).withAlign(searchField, Alignment.MIDDLE_RIGHT).withAlign(controlsBtn,
                    Alignment.MIDDLE_RIGHT);
        }

        prjList.showProjects();
    }

    @Override
    public Component gotoSubView(String viewId) {
        return myProjectTab.selectTab(viewId);
    }

    private void buildComponents() {
        Integer prjId = CurrentProjectVariables.getProjectId();

        myProjectTab.addTab(
                constructProjectDashboardComponent(),
                ProjectTypeConstants.DASHBOARD,
                1,
                AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD),
                GenericLinkUtils.URL_PREFIX_PARAM
                        + ProjectLinkGenerator.generateProjectLink(prjId));

        if (CurrentProjectVariables.hasMessageFeature()) {
            myProjectTab.addTab(
                    constructProjectMessageComponent(),
                    ProjectTypeConstants.MESSAGE,
                    2,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator.generateMessagesLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.MESSAGE);
        }

        if (CurrentProjectVariables.hasPhaseFeature()) {
            myProjectTab
                    .addTab(constructProjectMilestoneComponent(),
                            ProjectTypeConstants.MILESTONE,
                            3,
                            AppContext
                                    .getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE),
                            GenericLinkUtils.URL_PREFIX_PARAM
                                    + ProjectLinkGenerator
                                    .generateMilestonesLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.MILESTONE);
        }

        if (CurrentProjectVariables.hasTaskFeature()) {
            myProjectTab.addTab(
                    constructTaskDashboardComponent(),
                    ProjectTypeConstants.TASK,
                    4,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TASK),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator
                            .generateTaskDashboardLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.TASK);
        }

        if (CurrentProjectVariables.hasBugFeature()) {
            myProjectTab.addTab(
                    constructProjectBugComponent(),
                    ProjectTypeConstants.BUG,
                    5,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_BUG),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator.generateProjectLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.BUG);
        }

        if (CurrentProjectVariables.hasPageFeature()) {
            myProjectTab.addTab(
                    constructProjectPageComponent(),
                    ProjectTypeConstants.PAGE,
                    6,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_PAGE),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator.generateProjectLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.PAGE);
        }

        if (CurrentProjectVariables.hasFileFeature()) {
            myProjectTab.addTab(
                    constructProjectFileComponent(),
                    ProjectTypeConstants.FILE,
                    7,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator
                            .generateFileDashboardLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.FILE);
        }

        if (CurrentProjectVariables.hasRiskFeature()) {
            myProjectTab.addTab(
                    constructProjectRiskComponent(),
                    ProjectTypeConstants.RISK,
                    8,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_RISK),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator.generateRisksLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.RISK);
        }

        if (CurrentProjectVariables.hasProblemFeature()) {
            myProjectTab.addTab(
                    constructProjectProblemComponent(),
                    ProjectTypeConstants.PROBLEM,
                    9,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_PROBLEM),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator.generateProblemsLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.PROBLEM);
        }

        if (CurrentProjectVariables.hasTimeFeature()) {
            myProjectTab.addTab(
                    constructTimeTrackingComponent(),
                    ProjectTypeConstants.TIME,
                    10,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator
                            .generateTimeReportLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.TIME);
        }

        if (CurrentProjectVariables.hasStandupFeature()) {
            myProjectTab.addTab(
                    constructProjectStandupMeeting(),
                    ProjectTypeConstants.STANDUP,
                    11,
                    AppContext.getMessage(ProjectCommonI18nEnum.VIEW_STANDAUP),
                    GenericLinkUtils.URL_PREFIX_PARAM
                            + ProjectLinkGenerator
                            .generateStandupDashboardLink(prjId));
        } else {
            myProjectTab.removeTab(ProjectTypeConstants.STANDUP);
        }

        myProjectTab.addTab(
                constructProjectUsers(),
                ProjectTypeConstants.MEMBER,
                12,
                AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MEMBER),
                GenericLinkUtils.URL_PREFIX_PARAM
                        + ProjectLinkGenerator.generateUsersLink(prjId));
    }

    @Override
    public void gotoUsersAndGroup(ScreenData<?> data) {
        userPresenter.go(ProjectViewImpl.this, data);
    }

    @Override
    public void gotoTaskList(ScreenData<?> data) {
        taskPresenter.go(ProjectViewImpl.this, data);
    }

    @Override
    public void gotoPageView(ScreenData<?> data) {
        pagePresenter.go(ProjectViewImpl.this, data);
    }

    @Override
    public void gotoRiskView(ScreenData<?> data) {
        riskPresenter.go(ProjectViewImpl.this, data);
    }

    @Override
    public void gotoTimeTrackingView(ScreenData<?> data) {
        timePresenter.go(ProjectViewImpl.this, data);
    }

    @Override
    public void gotoBugView(ScreenData<?> data) {
        trackerPresenter.go(ProjectViewImpl.this, data);
    }

    @Override
    public void gotoMilestoneView(ScreenData<?> data) {
        milestonesPresenter.go(ProjectViewImpl.this, data);
    }

    @Override
    public void gotoStandupReportView(ScreenData<?> data) {
        standupPresenter.go(ProjectViewImpl.this, data);
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
        trackerPresenter = PresenterResolver
                .getPresenter(TrackerPresenter.class);
        return trackerPresenter.getView();
    }

    private Component constructProjectFileComponent() {
        filePresenter = PresenterResolver.getPresenter(IFilePresenter.class);
        return filePresenter.getView();
    }

    @Override
    public void updateProjectFeatures() {
        buildComponents();
    }
}
