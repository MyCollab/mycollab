/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.mycollab.common.GenericLinkUtils;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.file.PathUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.file.FilePresenter;
import com.mycollab.module.project.view.message.MessagePresenter;
import com.mycollab.module.project.view.milestone.MilestonePresenter;
import com.mycollab.module.project.view.page.PagePresenter;
import com.mycollab.module.project.view.parameters.*;
import com.mycollab.module.project.view.reports.IProjectReportPresenter;
import com.mycollab.module.project.view.settings.UserSettingPresenter;
import com.mycollab.module.project.view.ticket.TicketPresenter;
import com.mycollab.module.project.view.time.IFinancePresenter;
import com.mycollab.module.project.view.user.ProjectDashboardPresenter;
import com.mycollab.module.project.view.user.ProjectInfoComponent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.*;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.mycollab.vaadin.web.ui.VerticalTabsheet.TabImpl;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.TabSheet.Tab;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectViewImpl extends AbstractVerticalPageView implements ProjectView {
    private ProjectViewWrap viewWrap;

    @Override
    public void initView(final SimpleProject project) {
        removeAllComponents();
        viewWrap = new ProjectViewWrap(project);
        ControllerRegistry.addController(new ProjectController(this));

        this.with(viewWrap).expand(viewWrap);
    }

    @Override
    public void displaySearchResult(String value) {
        viewWrap.displaySearchResult(value);
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
        private VerticalTabsheet myProjectTab;

        private ProjectDashboardPresenter dashboardPresenter;
        private MessagePresenter messagePresenter;
        private MilestonePresenter milestonesPresenter;
        private TicketPresenter ticketPresenter;
        private PagePresenter pagePresenter;
        private FilePresenter filePresenter;
        private IFinancePresenter financePresenter;
        private IProjectReportPresenter reportPresenter;
        private UserSettingPresenter userPresenter;

        ProjectViewWrap(SimpleProject project) {
            this.setWidth("100%");
            this.addStyleName("project-view");

            myProjectTab = new VerticalTabsheet();
            myProjectTab.setSizeFull();
            myProjectTab.setNavigatorWidth("100%");
            myProjectTab.setNavigatorStyleName("sidebar-menu");

            myProjectTab.addSelectedTabChangeListener(selectedTabChangeEvent -> {
                Tab tab = ((VerticalTabsheet) selectedTabChangeEvent.getSource()).getSelectedTab();
                String caption = ((TabImpl) tab).getTabId();
                if (ProjectTypeConstants.MESSAGE.equals(caption)) {
                    messagePresenter.go(ProjectViewImpl.this, null);
                } else if (ProjectTypeConstants.MILESTONE.equals(caption)) {
                    milestonesPresenter.go(ProjectViewImpl.this, new MilestoneScreenData.Roadmap());
                } else if (ProjectTypeConstants.TICKET.equals(caption)) {
                    ticketPresenter.go(ProjectViewImpl.this, null);
                } else if (ProjectTypeConstants.FILE.equals(caption)) {
                    filePresenter.go(ProjectViewImpl.this, new FileScreenData.GotoDashboard());
                } else if (ProjectTypeConstants.PAGE.equals(caption)) {
                    pagePresenter.go(ProjectViewImpl.this,
                            new PageScreenData.Search(PathUtils.getProjectDocumentPath(AppUI.getAccountId(), project.getId())));
                } else if (ProjectTypeConstants.DASHBOARD.equals(caption)) {
                    dashboardPresenter.go(ProjectViewImpl.this, null);
                } else if (ProjectTypeConstants.MEMBER.equals(caption)) {
                    ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                    criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                    criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET));
                    userPresenter.go(ProjectViewImpl.this, new ProjectMemberScreenData.Search(criteria));
                } else if (ProjectTypeConstants.FINANCE.equals(caption)) {
                    financePresenter.go(ProjectViewImpl.this, null);
                } else if (ProjectTypeConstants.REPORTS.equals(caption)) {
                    reportPresenter.go(ProjectViewImpl.this, null);
                }
            });

            VerticalLayout contentWrapper = myProjectTab.getContentWrapper();
            contentWrapper.addStyleName("main-content");

            MVerticalLayout topPanel = new MVerticalLayout(new ProjectInfoComponent(project)).withSpacing(false).withMargin(new MarginInfo(false, true, false, true))
                    .withFullWidth().withStyleName("top-panel").withHeightUndefined().withFullWidth();
            contentWrapper.addComponentAsFirst(topPanel);

            CssLayout navigatorWrapper = myProjectTab.getNavigatorWrapper();
            navigatorWrapper.setWidth("250px");

            buildComponents();
            this.addComponent(myProjectTab);

            if (project.getContextask() == null || project.getContextask()) {
                ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
                searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                searchCriteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET));
                ProjectMemberService prjMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                int totalMembers = prjMemberService.getTotalCount(searchCriteria);
                if (totalMembers < 2) {
                    UI.getCurrent().addWindow(new AskToAddMoreMembersWindow());
                }
            }
        }

        void displaySearchResult(String value) {
            dashboardPresenter.go(ProjectViewImpl.this, new ProjectScreenData.SearchItem(value));
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
                    UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD),
                    GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectLink(prjId),
                    ProjectAssetsManager.getAsset(ProjectTypeConstants.DASHBOARD));

            if (CurrentProjectVariables.hasMessageFeature()) {
                myProjectTab.addTab(constructProjectMessageComponent(), ProjectTypeConstants.MESSAGE, 2,
                        UserUIContext.getMessage(MessageI18nEnum.LIST),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateMessagesLink(prjId),
                        ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.MESSAGE);
            }

            if (CurrentProjectVariables.hasPhaseFeature()) {
                myProjectTab.addTab(constructProjectMilestoneComponent(), ProjectTypeConstants.MILESTONE, 3,
                        UserUIContext.getMessage(MilestoneI18nEnum.LIST),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateMilestonesLink(prjId),
                        ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.MILESTONE);
            }

            if (CurrentProjectVariables.hasTicketFeature()) {
                myProjectTab.addTab(constructTaskDashboardComponent(),
                        ProjectTypeConstants.TICKET, 4, UserUIContext.getMessage(TicketI18nEnum.LIST),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateTicketDashboardLink(prjId),
                        ProjectAssetsManager.getAsset(ProjectTypeConstants.TICKET));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.TICKET);
            }

            if (CurrentProjectVariables.hasPageFeature()) {
                myProjectTab.addTab(constructProjectPageComponent(), ProjectTypeConstants.PAGE, 6,
                        UserUIContext.getMessage(PageI18nEnum.LIST),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectLink(prjId),
                        ProjectAssetsManager.getAsset(ProjectTypeConstants.PAGE));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.PAGE);
            }

            if (CurrentProjectVariables.hasFileFeature()) {
                myProjectTab.addTab(constructProjectFileComponent(), ProjectTypeConstants.FILE, 7,
                        UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateFileDashboardLink(prjId),
                        ProjectAssetsManager.getAsset(ProjectTypeConstants.FILE));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.FILE);
            }

            if ((CurrentProjectVariables.hasTimeFeature() || CurrentProjectVariables.hasInvoiceFeature())
                    && !SiteConfiguration.isCommunityEdition()) {
                myProjectTab.addTab(constructTimeTrackingComponent(), ProjectTypeConstants.FINANCE, 10,
                        UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FINANCE),
                        GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateTimeReportLink(prjId),
                        ProjectAssetsManager.getAsset(ProjectTypeConstants.FINANCE));
            } else {
                myProjectTab.removeTab(ProjectTypeConstants.FINANCE);
            }

//            myProjectTab.addTab(constructProjectReportComponent(), ProjectTypeConstants.REPORTS, 12,
//                    UserUIContext.getMessage(PageI18nEnum.LIST),
//                    GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateProjectLink(prjId),
//                    ProjectAssetsManager.getAsset(ProjectTypeConstants.PAGE));

            myProjectTab.addTab(constructProjectUsers(), ProjectTypeConstants.MEMBER, 13,
                    UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_MEMBER),
                    GenericLinkUtils.URL_PREFIX_PARAM + ProjectLinkGenerator.generateUsersLink(prjId),
                    ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER));

            myProjectTab.addToggleNavigatorControl();
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

        private Component constructTimeTrackingComponent() {
            financePresenter = PresenterResolver.getPresenter(IFinancePresenter.class);
            return financePresenter.getView();
        }

        private Component constructTaskDashboardComponent() {
            ticketPresenter = PresenterResolver.getPresenter(TicketPresenter.class);
            return ticketPresenter.getView();
        }

        private Component constructProjectFileComponent() {
            filePresenter = PresenterResolver.getPresenter(FilePresenter.class);
            return filePresenter.getView();
        }
    }

    private static class AskToAddMoreMembersWindow extends MWindow {
        AskToAddMoreMembersWindow() {
            super(UserUIContext.getMessage(GenericI18Enum.OPT_QUESTION));
            MVerticalLayout content = new MVerticalLayout();
            this.withWidth("600px").withResizable(false).withModal(true).withContent(content).withCenter();

            content.with(new Label(UserUIContext.getMessage(ProjectI18nEnum.OPT_ASK_TO_ADD_MEMBERS)));

            MButton skipBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_SKIP), clickEvent -> {
                ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
                SimpleProject project = CurrentProjectVariables.getProject();
                project.setContextask(false);
                projectService.updateSelectiveWithSession(project, UserUIContext.getUsername());
                close();
            }).withStyleName(WebThemes.BUTTON_OPTION);

            MButton addNewMembersBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.ACTION_ADD_MEMBERS), clickEvent -> {
                close();
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null));
            }).withStyleName(WebThemes.BUTTON_ACTION);

            MHorizontalLayout btnControls = new MHorizontalLayout(skipBtn, addNewMembersBtn);
            content.with(btnControls).withAlign(btnControls, Alignment.MIDDLE_RIGHT);
        }
    }
}
