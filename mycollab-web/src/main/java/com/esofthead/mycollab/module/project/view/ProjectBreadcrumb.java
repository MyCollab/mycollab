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

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.PathUtils;
import com.esofthead.mycollab.module.page.domain.Folder;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.page.service.PageService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.events.*;
import com.esofthead.mycollab.module.project.i18n.*;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.web.ui.CommonUIFactory;
import com.esofthead.mycollab.vaadin.web.ui.utils.LabelStringGenerator;
import com.vaadin.breadcrumb.Breadcrumb;
import com.vaadin.breadcrumb.BreadcrumbLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectBreadcrumb extends Breadcrumb implements CacheableComponent {
    private static final long serialVersionUID = 1L;
    private static LabelStringGenerator menuLinkGenerator = new BreadcrumbLabelStringGenerator();

    private SimpleProject project;

    private Button homeBtn;

    public ProjectBreadcrumb() {
        this.setShowAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        this.setHideAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        this.setUseDefaultClickBehaviour(false);
        homeBtn = new Button(null, new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this,
                        new PageActionChain(new ProjectScreenData.Goto(project.getId()))));
            }
        });
        this.addLink(homeBtn);
    }

    @Override
    public void addLink(Button newBtn) {
        if (getComponentCount() > 0)
            homeBtn.setCaption(null);
        else
            homeBtn.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));

        super.addLink(newBtn);
    }

    @Override
    public void select(int id) {
        if (id == 0) {
            homeBtn.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));
        } else {
            homeBtn.setCaption(null);
        }
        super.select(id);
    }

    public void setProject(SimpleProject project) {
        this.project = project;
        this.select(0);
    }

    public void gotoSearchProjectItems() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH)));
        AppContext.addFragment(ProjectLinkGenerator.generateProjectLink(project.getId()),
                AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
    }

    public void gotoMessageList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(MessageI18nEnum.LIST)));
        AppContext.addFragment(ProjectLinkGenerator.generateMessagesLink(project.getId()),
                AppContext.getMessage(MessageI18nEnum.LIST));
    }

    public void gotoMessage(Message message) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(MessageI18nEnum.LIST), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
            }
        }));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(message.getTitle()));
        AppContext.addFragment(ProjectLinkGenerator.generateMessagePreviewLink(project.getId(), message.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(MessageI18nEnum.SINGLE), message.getTitle()));
    }

    public void gotoRiskList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(RiskI18nEnum.LIST)));
        AppContext.addFragment(ProjectLinkGenerator.generateRisksLink(project.getId()),
                AppContext.getMessage(RiskI18nEnum.LIST));
    }

    public void gotoRiskRead(Risk risk) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(RiskI18nEnum.LIST), new GotoRiskListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(risk.getRiskname()));
        AppContext.addFragment(ProjectLinkGenerator.generateRiskPreviewLink(project.getId(), risk.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(RiskI18nEnum.SINGLE), risk.getRiskname()));
    }

    public void gotoRiskEdit(final Risk risk) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(RiskI18nEnum.LIST), new GotoRiskListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(risk.getRiskname(), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new RiskEvent.GotoRead(this, risk.getId()));
            }
        }));
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment(ProjectLinkGenerator.generateRiskEditLink(project.getId(), risk.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        AppContext.getMessage(RiskI18nEnum.SINGLE), risk.getRiskname()));
    }

    public void gotoRiskAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(RiskI18nEnum.LIST), new GotoRiskListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppContext.addFragment(ProjectLinkGenerator.generateRiskAddLink(project.getId()),
                AppContext.getMessage(RiskI18nEnum.NEW));
    }

    private static class GotoRiskListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new RiskEvent.GotoList(this, null));
        }
    }

    public void gotoMilestoneList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(MilestoneI18nEnum.LIST)));
        AppContext.addFragment(ProjectLinkGenerator.generateMilestonesLink(project.getId()),
                AppContext.getMessage(MilestoneI18nEnum.LIST));
    }

    public void gotoRoadmap() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_ROADMAP)));
        AppContext.addFragment("project/roadmap/" + UrlEncodeDecoder.encode(project.getId()), "Roadmap");
    }

    public void gotoMilestoneRead(Milestone milestone) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(milestone.getName()));
        AppContext.addFragment(ProjectLinkGenerator.generateMilestonePreviewLink(project.getId(), milestone.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(MilestoneI18nEnum.SINGLE), milestone.getName()));
    }

    public void gotoMilestoneEdit(final Milestone milestone) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(milestone.getName(), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoRead(this, milestone.getId()));
            }
        }));
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment("project/milestone/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + milestone.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        AppContext.getMessage(MilestoneI18nEnum.SINGLE), milestone.getName()));
    }

    public void gotoMilestoneAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(MilestoneI18nEnum.NEW)));
        AppContext.addFragment("project/milestone/add/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(MilestoneI18nEnum.NEW));
    }

    private static class GotoMilestoneListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
        }
    }

    private void buildPageBreadcrumbChain() {
        String basePath = PathUtils.getProjectDocumentPath(AppContext.getAccountId(), CurrentProjectVariables.getProjectId());
        String currentPath = CurrentProjectVariables.getCurrentPagePath();

        this.addLink(new Button(AppContext.getMessage(PageI18nEnum.LIST), new GotoPageListListener(basePath)));
        this.setLinkEnabled(true, 1);

        String extraPath = currentPath.substring(basePath.length());
        if (extraPath.startsWith("/")) {
            extraPath = extraPath.substring(1);
        }
        if (!extraPath.equals("")) {
            PageService wikiService = AppContextUtil.getSpringBean(PageService.class);

            String[] subPath = extraPath.split("/");
            StringBuilder tempPath = new StringBuilder();
            for (String var : subPath) {
                tempPath.append("/").append(var);
                String folderPath = basePath + tempPath.toString();
                Folder folder = wikiService.getFolder(folderPath);
                if (folder != null) {
                    this.addLink(new Button(folder.getName(), new GotoPageListListener(folderPath)));
                } else {
                    return;
                }

            }
        }
    }

    public void gotoPageList() {
        this.select(0);
        buildPageBreadcrumbChain();
        AppContext.addFragment(ProjectLinkGenerator.generatePagesLink(project.getId(),
                CurrentProjectVariables.getCurrentPagePath()), AppContext.getMessage(PageI18nEnum.LIST));
    }

    public void gotoPageAdd() {
        this.select(0);
        buildPageBreadcrumbChain();
        this.addLink(new Button(AppContext.getMessage(PageI18nEnum.NEW)));
        AppContext.addFragment(ProjectLinkGenerator.generatePageAdd(
                project.getId(), CurrentProjectVariables.getCurrentPagePath()),
                AppContext.getMessage(PageI18nEnum.NEW));
    }

    public void gotoPageRead(Page page) {
        this.select(0);
        buildPageBreadcrumbChain();
        this.addLink(new Button(StringUtils.trim(page.getSubject(), 50)));
        AppContext.addFragment(ProjectLinkGenerator.generatePageRead(project.getId(), page.getPath()),
                AppContext.getMessage(PageI18nEnum.DETAIL));
    }

    public void gotoPageEdit(Page page) {
        this.select(0);
        buildPageBreadcrumbChain();

        AppContext.addFragment(ProjectLinkGenerator.generatePageEdit(project.getId(), page.getPath()),
                AppContext.getMessage(PageI18nEnum.DETAIL));
    }

    private static class GotoPageListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        private String path;

        public GotoPageListListener(String path) {
            this.path = path;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new PageEvent.GotoList(this, path));
        }
    }

    public void gotoTaskDashboard(String query) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(TaskI18nEnum.LIST)));
        String fragment = (StringUtils.isNotBlank(query)) ? ProjectLinkGenerator.generateTaskDashboardLink(project.getId()) + "?" + query : ProjectLinkGenerator.generateTaskDashboardLink(project.getId());
        AppContext.addFragment(fragment, AppContext.getMessage(BreadcrumbI18nEnum.FRA_TASK_DASHBOARD));
    }

    public void gotoTaskAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(TaskI18nEnum.LIST), new GotoTaskAssignmentDashboard()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(TaskI18nEnum.NEW)));
        AppContext.addFragment("project/task/add/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(TaskI18nEnum.NEW));
    }

    public void gotoGanttView() {
        this.select(0);
        this.addLink(new Button("Gantt chart"));
        AppContext.addFragment("project/gantt/" + UrlEncodeDecoder.encode(project.getId()), "Gantt chart");
    }

    public void gotoTaskKanbanView() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(TaskI18nEnum.LIST), new GotoTaskAssignmentDashboard()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button("Kanban"));
        AppContext.addFragment("project/task/kanban/" + UrlEncodeDecoder.encode(project.getId()), "Kanban View");
    }

    public void gotoCalendar() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_CALENDAR)));
        AppContext.addFragment("project/calendar/" + UrlEncodeDecoder.encode(project.getId()), "Calendar");
    }

    public void gotoTaskRead(SimpleTask task) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(TaskI18nEnum.LIST), new GotoTaskAssignmentDashboard()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                AppContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname())));
        AppContext.addFragment(ProjectLinkGenerator.generateTaskPreviewLink(task.getTaskkey(), task.getProjectShortname()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname()));
    }

    public void gotoTaskEdit(final SimpleTask task) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(TaskI18nEnum.LIST),
                new GotoTaskAssignmentDashboard()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                AppContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname()), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, task.getId()));
            }
        }));
        this.setLinkEnabled(true, 2);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment(ProjectLinkGenerator.generateTaskEditLink(task.getTaskkey(), task.getProjectShortname()),
                AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        AppContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname()));
    }

    private static class GotoTaskAssignmentDashboard implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new TaskEvent.GotoDashboard(this, null));
        }
    }

    public void gotoBugKanbanView() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button("Kanban"));
        AppContext.addFragment("project/bug/kanban/" + UrlEncodeDecoder.encode(project.getId()), "Kanban View");
    }

    public void gotoBugList(String query) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(BugI18nEnum.LIST)));
        String fragment = (StringUtils.isNotBlank(query)) ? ProjectLinkGenerator.generateBugsLink(project.getId()) + "?" +
                query : ProjectLinkGenerator.generateBugsLink(project.getId());
        AppContext.addFragment(fragment, AppContext.getMessage(BugI18nEnum.LIST));
    }

    public void gotoBugAdd() {
        this.select(0);

        this.addLink(new Button(AppContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        this.setLinkEnabled(true, 1);

        this.addLink(new Button(AppContext.getMessage(BugI18nEnum.NEW)));
        AppContext.addFragment("project/bug/add/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(BugI18nEnum.NEW));
    }

    public void gotoBugEdit(final SimpleBug bug) {
        this.select(0);

        this.addLink(new Button(AppContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        this.setLinkEnabled(true, 1);

        this.addLink(generateBreadcrumbLink(bug.getSummary(), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, bug.getId()));
            }
        }));
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment(ProjectLinkGenerator.generateBugEditLink(bug.getBugkey(),
                bug.getProjectShortName()), AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                AppContext.getMessage(BugI18nEnum.SINGLE), bug.getSummary()));
    }

    public void gotoBugRead(SimpleBug bug) {
        this.select(0);

        this.addLink(new Button(AppContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        this.setLinkEnabled(true, 1);

        this.addLink(generateBreadcrumbLink(bug.getSummary()));
        AppContext.addFragment(ProjectLinkGenerator.generateBugPreviewLink(bug.getBugkey(), bug.getProjectShortName()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(BugI18nEnum.SINGLE), bug.getSummary()));
    }

    public void gotoVersionList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(VersionI18nEnum.LIST)));
        AppContext.addFragment("project/version/list/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(VersionI18nEnum.LIST));
    }

    public void gotoVersionAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppContext.addFragment("project/version/add/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(VersionI18nEnum.NEW));
    }

    public void gotoVersionEdit(final Version version) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(version.getVersionname(), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId()));
            }
        }));
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment("project/version/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + version.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        AppContext.getMessage(VersionI18nEnum.SINGLE), version.getVersionname()));
    }

    public void gotoVersionRead(Version version) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(version.getVersionname()));
        AppContext.addFragment(ProjectLinkGenerator.generateBugVersionPreviewLink(project.getId(), version.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(VersionI18nEnum.SINGLE), version.getVersionname()));
    }

    private static class GotoVersionListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
        }
    }

    public void gotoTagList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG)));
        AppContext.addFragment("project/tag/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG));
    }

    public void gotoFavoriteList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES)));
        AppContext.addFragment("project/favorite/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES));
    }

    public void gotoComponentList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ComponentI18nEnum.LIST)));
        AppContext.addFragment("project/component/list/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(ComponentI18nEnum.LIST));
    }

    public void gotoComponentAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppContext.addFragment("project/component/add/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(ComponentI18nEnum.NEW));
    }

    public void gotoComponentEdit(final Component component) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(component.getComponentname(), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, component.getId()));
            }
        }));
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment("project/component/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + component.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        AppContext.getMessage(ComponentI18nEnum.SINGLE), component.getComponentname()));
    }

    public void gotoComponentRead(Component component) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        this.addLink(generateBreadcrumbLink(component.getComponentname()));
        AppContext.addFragment(ProjectLinkGenerator.generateBugComponentPreviewLink(project.getId(), component.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(ComponentI18nEnum.SINGLE), component.getComponentname()));
    }

    public void gotoTimeTrackingList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME)));
        AppContext.addFragment(ProjectLinkGenerator.generateTimeReportLink(project.getId()),
                AppContext.getMessage(BreadcrumbI18nEnum.FRA_TIME_TRACKING));
    }

    public void gotoInvoiceView() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(InvoiceI18nEnum.LIST)));
        AppContext.addFragment(ProjectLinkGenerator.generateInvoiceListLink(project.getId()),
                AppContext.getMessage(InvoiceI18nEnum.LIST));
    }

    public void gotoFileList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE)));
        AppContext.addFragment(ProjectLinkGenerator.generateFileDashboardLink(project.getId()),
                AppContext.getMessage(BreadcrumbI18nEnum.FRA_FILES));
    }

    public void gotoUserList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectMemberI18nEnum.LIST)));
        AppContext.addFragment(ProjectLinkGenerator.generateUsersLink(project.getId()),
                AppContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBERS));
    }

    public void gotoUserAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES)));
        AppContext.addFragment("project/user/add/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(BreadcrumbI18nEnum.FRA_INVITE_MEMBERS));
    }

    public void gotoUserRead(SimpleProjectMember member) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(member.getMemberFullName()));
        AppContext.addFragment("project/user/preview/" + UrlEncodeDecoder.encode(project.getId() + "/"
                + member.getUsername()), AppContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBER_READ, member.getMemberFullName()));
    }

    public void gotoUserEdit(SimpleProjectMember member) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(member.getMemberFullName()));
        AppContext.addFragment("project/user/edit/" + UrlEncodeDecoder.encode(project.getId() + "/"
                + member.getId()), AppContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBER_EDIT, member.getMemberFullName()));
    }

    public void gotoRoleList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectRoleI18nEnum.LIST)));
        AppContext.addFragment("project/role/list/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(ProjectRoleI18nEnum.LIST));
    }

    public void gotoRoleRead(SimpleProjectRole role) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoRoleListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(role.getRolename()));
        AppContext.addFragment("project/role/preview/" + UrlEncodeDecoder.encode(project.getId() + "/" + role.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        AppContext.getMessage(ProjectRoleI18nEnum.SINGLE), role.getRolename()));
    }

    public void gotoProjectSetting() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.FRA_SETTING), new GotoNotificationSettingListener()));
        AppContext.addFragment("project/setting/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(BreadcrumbI18nEnum.FRA_SETTING));
    }

    public void gotoRoleAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoRoleListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppContext.addFragment("project/role/add/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(ProjectRoleI18nEnum.NEW));
    }

    public void gotoRoleEdit(ProjectRole role) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoUserListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(role.getRolename()));
        AppContext.addFragment("project/role/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + role.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        AppContext.getMessage(ProjectRoleI18nEnum.SINGLE), role.getRolename()));
    }

    private static class GotoNotificationSettingListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new ProjectNotificationEvent.GotoList(this, null));
        }
    }

    private static class GotoRoleListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
        }
    }

    private static class GotoUserListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
        }
    }

    private static class GotoComponentListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new BugComponentEvent.GotoList(this, null));
        }
    }

    private static class GotoBugListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null));
        }
    }

    public void gotoProjectDashboard() {
        this.select(0);
        AppContext.addFragment(ProjectLinkGenerator.generateProjectLink(project.getId()),
                AppContext.getMessage(GenericI18Enum.VIEW_DASHBOARD));
    }

    public void gotoProjectEdit() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment("project/edit/" + UrlEncodeDecoder.encode(project.getId()),
                AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        AppContext.getMessage(ProjectI18nEnum.SINGLE), project.getName()));
    }

    @Override
    public int getComponentCount() {
        if (getCompositionRoot() != null) {
            final BreadcrumbLayout compositionRoot = (BreadcrumbLayout) getCompositionRoot();
            return compositionRoot.getComponentCount();
        }
        return super.getComponentCount();
    }

    private static Button generateBreadcrumbLink(String linkName) {
        return CommonUIFactory.createButtonTooltip(menuLinkGenerator.handleText(linkName), linkName);
    }

    private static Button generateBreadcrumbLink(String linkName, Button.ClickListener listener) {
        return CommonUIFactory.createButtonTooltip(menuLinkGenerator.handleText(linkName), linkName, listener);
    }

    private static class BreadcrumbLabelStringGenerator implements LabelStringGenerator {
        @Override
        public String handleText(String value) {
            if (value.length() > 35) {
                return value.substring(0, 35) + "...";
            }
            return value;
        }
    }
}
