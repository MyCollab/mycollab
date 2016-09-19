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
package com.mycollab.module.project.view;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.file.PathUtils;
import com.mycollab.module.page.domain.Folder;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.page.service.PageService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.*;
import com.mycollab.module.project.events.*;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.CommonUIFactory;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.utils.LabelStringGenerator;
import com.vaadin.breadcrumb.Breadcrumb;
import com.vaadin.breadcrumb.BreadcrumbLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectBreadcrumb extends MHorizontalLayout implements CacheableComponent {
    private static final long serialVersionUID = 1L;
    private static LabelStringGenerator menuLinkGenerator = new BreadcrumbLabelStringGenerator();

    private Breadcrumb breadcrumb;
    private MHorizontalLayout controlsLayout;
    private SimpleProject project;

    private Button homeBtn;

    public ProjectBreadcrumb() {
        homeBtn = new Button(null, clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null)));
        breadcrumb = new Breadcrumb() {
            @Override
            public void addLink(Button newBtn) {
                if (getComponentCount() > 0)
                    homeBtn.setCaption(null);
                else
                    homeBtn.setCaption(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));

                super.addLink(newBtn);
            }

            @Override
            public void select(int id) {
                if (id == 0) {
                    homeBtn.setCaption(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));
                    controlsLayout.removeAllComponents();
                } else {
                    homeBtn.setCaption(null);
                }
                super.select(id);
            }

            @Override
            public int getComponentCount() {
                if (getCompositionRoot() != null) {
                    final BreadcrumbLayout compositionRoot = (BreadcrumbLayout) getCompositionRoot();
                    return compositionRoot.getComponentCount();
                }
                return super.getComponentCount();
            }
        };
        breadcrumb.setShowAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        breadcrumb.setHideAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        breadcrumb.setUseDefaultClickBehaviour(false);
        breadcrumb.addLink(homeBtn);

        controlsLayout = new MHorizontalLayout();
        with(breadcrumb, controlsLayout).expand(breadcrumb).alignAll(Alignment.MIDDLE_RIGHT);
    }

    public void setProject(SimpleProject project) {
        this.project = project;
        breadcrumb.select(0);
    }

    public void gotoSearchProjectItems() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateProjectLink(project.getId()),
                UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
    }

    public void gotoMessageList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MessageI18nEnum.LIST)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateMessagesLink(project.getId()),
                UserUIContext.getMessage(MessageI18nEnum.LIST));
    }

    public void gotoMessage(Message message) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MessageI18nEnum.LIST),
                clickEvent -> EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null))));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(message.getTitle()));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateMessagePreviewLink(project.getId(), message.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(MessageI18nEnum.SINGLE), message.getTitle()));
    }

    public void gotoRiskList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(RiskI18nEnum.LIST)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateRisksLink(project.getId()),
                UserUIContext.getMessage(RiskI18nEnum.LIST));
    }

    public void gotoRiskRead(Risk risk) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(RiskI18nEnum.LIST), new GotoRiskListListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(risk.getRiskname()));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateRiskPreviewLink(project.getId(), risk.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(RiskI18nEnum.SINGLE), risk.getRiskname()));
    }

    public void gotoRiskEdit(final Risk risk) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(RiskI18nEnum.LIST), new GotoRiskListListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(risk.getRiskname(),
                clickEvent -> EventBusFactory.getInstance().post(new RiskEvent.GotoRead(this, risk.getId()))));
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateRiskEditLink(project.getId(), risk.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(RiskI18nEnum.SINGLE), risk.getRiskname()));
    }

    public void gotoRiskAdd() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(RiskI18nEnum.LIST), new GotoRiskListListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateRiskAddLink(project.getId()),
                UserUIContext.getMessage(RiskI18nEnum.NEW));
    }

    private static class GotoRiskListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new RiskEvent.GotoList(this, null));
        }
    }

    public void gotoMilestoneList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateMilestonesLink(project.getId()),
                UserUIContext.getMessage(MilestoneI18nEnum.LIST));
    }

    public void gotoRoadmap() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_ROADMAP)));
        MyCollabUI.addFragment("project/roadmap/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_ROADMAP));
    }

    public void gotoKanban() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN)));
        MyCollabUI.addFragment("project/milestone/kanban/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN));
    }

    public void gotoMilestoneRead(Milestone milestone) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(milestone.getName()));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateMilestonePreviewLink(project.getId(), milestone.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), milestone.getName()));
    }

    public void gotoMilestoneEdit(final Milestone milestone) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(milestone.getName(),
                clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoRead(this, milestone.getId()))));
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        MyCollabUI.addFragment("project/milestone/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + milestone.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), milestone.getName()));
    }

    public void gotoMilestoneAdd() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.NEW)));
        MyCollabUI.addFragment("project/milestone/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(MilestoneI18nEnum.NEW));
    }

    private static class GotoMilestoneListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
        }
    }

    private void buildPageBreadcrumbChain() {
        String basePath = PathUtils.getProjectDocumentPath(MyCollabUI.getAccountId(), CurrentProjectVariables.getProjectId());
        String currentPath = CurrentProjectVariables.getCurrentPagePath();

        breadcrumb.addLink(new Button(UserUIContext.getMessage(PageI18nEnum.LIST), new GotoPageListListener(basePath)));
        breadcrumb.setLinkEnabled(true, 1);

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
                    breadcrumb.addLink(new Button(folder.getName(), new GotoPageListListener(folderPath)));
                } else {
                    return;
                }
            }
        }
    }

    public void gotoPageList() {
        breadcrumb.select(0);
        buildPageBreadcrumbChain();
        MyCollabUI.addFragment(ProjectLinkGenerator.generatePagesLink(project.getId(),
                CurrentProjectVariables.getCurrentPagePath()), UserUIContext.getMessage(PageI18nEnum.LIST));
    }

    public void gotoPageAdd() {
        breadcrumb.select(0);
        buildPageBreadcrumbChain();
        breadcrumb.addLink(new Button(UserUIContext.getMessage(PageI18nEnum.NEW)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generatePageAdd(
                project.getId(), CurrentProjectVariables.getCurrentPagePath()),
                UserUIContext.getMessage(PageI18nEnum.NEW));
    }

    public void gotoPageRead(Page page) {
        breadcrumb.select(0);
        buildPageBreadcrumbChain();
        breadcrumb.addLink(new Button(StringUtils.trim(page.getSubject(), 50)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generatePageRead(project.getId(), page.getPath()),
                UserUIContext.getMessage(PageI18nEnum.DETAIL));
    }

    public void gotoPageEdit(Page page) {
        breadcrumb.select(0);
        buildPageBreadcrumbChain();

        MyCollabUI.addFragment(ProjectLinkGenerator.generatePageEdit(project.getId(), page.getPath()),
                UserUIContext.getMessage(PageI18nEnum.DETAIL));
    }

    private static class GotoPageListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        private String path;

        GotoPageListListener(String path) {
            this.path = path;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new PageEvent.GotoList(this, path));
        }
    }

    public void gotoTaskDashboard(String query) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(TaskI18nEnum.LIST)));
        String fragment = (StringUtils.isNotBlank(query)) ? ProjectLinkGenerator.generateTaskDashboardLink(project.getId()) + "?" + query : ProjectLinkGenerator.generateTaskDashboardLink(project.getId());
        MyCollabUI.addFragment(fragment, UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_TASK_DASHBOARD));
    }

    public void gotoTaskAdd() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(TaskI18nEnum.LIST), new GotoTaskAssignmentDashboard()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(TaskI18nEnum.NEW)));
        MyCollabUI.addFragment("project/task/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(TaskI18nEnum.NEW));
    }

    public void gotoGanttView() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_GANTT_CHART)));
        MyCollabUI.addFragment("project/gantt/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_GANTT_CHART));
    }

    public void gotoTaskKanbanView() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(TaskI18nEnum.LIST), new GotoTaskAssignmentDashboard()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN)));
        MyCollabUI.addFragment("project/task/kanban/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN));
    }

    public void gotoCalendar() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_CALENDAR)));
        MyCollabUI.addFragment("project/calendar/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_CALENDAR));
    }

    public void gotoTaskRead(SimpleTask task) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(TaskI18nEnum.LIST), new GotoTaskAssignmentDashboard()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname())));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateTaskPreviewLink(task.getTaskkey(), task.getProjectShortname()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname()));
    }

    public void gotoTaskEdit(final SimpleTask task) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(TaskI18nEnum.LIST),
                new GotoTaskAssignmentDashboard()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname()),
                clickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, task.getId()))));
        breadcrumb.setLinkEnabled(true, 2);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateTaskEditLink(task.getTaskkey(), task.getProjectShortname()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getTaskname()));
    }

    private static class GotoTaskAssignmentDashboard implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new TaskEvent.GotoDashboard(this, null));
        }
    }

    public void gotoBugKanbanView() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN)));
        MyCollabUI.addFragment("project/bug/kanban/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN));
    }

    public void gotoBugList(String query) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(BugI18nEnum.LIST)));
        String fragment = (StringUtils.isNotBlank(query)) ? ProjectLinkGenerator.generateBugsLink(project.getId()) + "?" +
                query : ProjectLinkGenerator.generateBugsLink(project.getId());
        MyCollabUI.addFragment(fragment, UserUIContext.getMessage(BugI18nEnum.LIST));
    }

    public void gotoBugAdd() {
        breadcrumb.select(0);

        breadcrumb.addLink(new Button(UserUIContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        breadcrumb.setLinkEnabled(true, 1);

        breadcrumb.addLink(new Button(UserUIContext.getMessage(BugI18nEnum.NEW)));
        MyCollabUI.addFragment("project/bug/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(BugI18nEnum.NEW));
    }

    public void gotoBugEdit(final SimpleBug bug) {
        breadcrumb.select(0);

        breadcrumb.addLink(new Button(UserUIContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        breadcrumb.setLinkEnabled(true, 1);

        breadcrumb.addLink(generateBreadcrumbLink(bug.getSummary(),
                clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, bug.getId()))));
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateBugEditLink(bug.getBugkey(),
                bug.getProjectShortName()), UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                UserUIContext.getMessage(BugI18nEnum.SINGLE), bug.getSummary()));
    }

    public void gotoBugRead(SimpleBug bug) {
        breadcrumb.select(0);

        breadcrumb.addLink(new Button(UserUIContext.getMessage(BugI18nEnum.LIST), new GotoBugListListener()));
        breadcrumb.setLinkEnabled(true, 1);

        breadcrumb.addLink(generateBreadcrumbLink(bug.getSummary()));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateBugPreviewLink(bug.getBugkey(), bug.getProjectShortName()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(BugI18nEnum.SINGLE), bug.getSummary()));
    }

    public void gotoVersionList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST)));
        MyCollabUI.addFragment("project/version/list/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(VersionI18nEnum.LIST));
    }

    public void gotoVersionAdd() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        MyCollabUI.addFragment("project/version/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(VersionI18nEnum.NEW));
    }

    public void gotoVersionEdit(final Version version) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(version.getVersionname(),
                clickEvent -> EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId()))));
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        MyCollabUI.addFragment("project/version/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + version.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(VersionI18nEnum.SINGLE), version.getVersionname()));
    }

    public void gotoVersionRead(Version version) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(version.getVersionname()));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateBugVersionPreviewLink(project.getId(), version.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(VersionI18nEnum.SINGLE), version.getVersionname()));
    }

    private static class GotoVersionListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
        }
    }

    public void gotoTagList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG)));
        MyCollabUI.addFragment("project/tag/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG));
    }

    public void gotoFavoriteList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES)));
        MyCollabUI.addFragment("project/favorite/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES));
    }

    public void gotoComponentList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST)));
        MyCollabUI.addFragment("project/component/list/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ComponentI18nEnum.LIST));
    }

    public void gotoComponentAdd() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        MyCollabUI.addFragment("project/component/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ComponentI18nEnum.NEW));
    }

    public void gotoComponentEdit(final Component component) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(component.getComponentname(),
                clickEvent -> EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, component.getId()))));
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        MyCollabUI.addFragment("project/component/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + component.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(ComponentI18nEnum.SINGLE), component.getComponentname()));
    }

    public void gotoComponentRead(Component component) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        breadcrumb.addLink(generateBreadcrumbLink(component.getComponentname()));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateBugComponentPreviewLink(project.getId(), component.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(ComponentI18nEnum.SINGLE), component.getComponentname()));
    }

    public void gotoTimeTrackingList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateTimeReportLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_TIME_TRACKING));
    }

    public void gotoInvoiceView() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(InvoiceI18nEnum.LIST)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateInvoiceListLink(project.getId()),
                UserUIContext.getMessage(InvoiceI18nEnum.LIST));
    }

    public void gotoFileList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateFileDashboardLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_FILES));
    }

    public void gotoUserList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST)));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateUsersLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBERS));
    }

    public void gotoUserAdd() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES)));
        MyCollabUI.addFragment("project/user/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_INVITE_MEMBERS));
    }

    public void gotoUserRead(SimpleProjectMember member) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(member.getMemberFullName()));
        MyCollabUI.addFragment("project/user/preview/" + UrlEncodeDecoder.encode(project.getId() + "/"
                + member.getUsername()), UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBER_READ, member.getMemberFullName()));
    }

    public void gotoUserEdit(SimpleProjectMember member) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(member.getMemberFullName()));
        MyCollabUI.addFragment("project/user/edit/" + UrlEncodeDecoder.encode(project.getId() + "/"
                + member.getId()), UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBER_EDIT, member.getMemberFullName()));
    }

    public void gotoRoleList() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST)));
        MyCollabUI.addFragment("project/role/list/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectRoleI18nEnum.LIST));
    }

    public void gotoRoleRead(SimpleProjectRole role) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoRoleListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(role.getRolename()));
        MyCollabUI.addFragment("project/role/preview/" + UrlEncodeDecoder.encode(project.getId() + "/" + role.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(ProjectRoleI18nEnum.SINGLE), role.getRolename()));
    }

    public void gotoProjectSetting() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_SETTING), new GotoNotificationSettingListener()));
        MyCollabUI.addFragment(ProjectLinkGenerator.generateProjectSettingLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_SETTING));
    }

    public void gotoRoleAdd() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoRoleListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        MyCollabUI.addFragment("project/role/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectRoleI18nEnum.NEW));
    }

    public void gotoRoleEdit(ProjectRole role) {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoUserListener()));
        breadcrumb.setLinkEnabled(true, 1);
        breadcrumb.addLink(generateBreadcrumbLink(role.getRolename()));
        MyCollabUI.addFragment("project/role/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + role.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(ProjectRoleI18nEnum.SINGLE), role.getRolename()));
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
        breadcrumb.select(0);
        MyCollabUI.addFragment(ProjectLinkGenerator.generateProjectLink(project.getId()),
                UserUIContext.getMessage(GenericI18Enum.VIEW_DASHBOARD));
        if (CurrentProjectVariables.isAdmin()) {
            controlsLayout.with(new MButton(UserUIContext.getMessage(GenericI18Enum.OPT_CUSTOMIZE_VIEW)).withIcon(FontAwesome.BRIEFCASE)
                    .withStyleName(WebUIConstants.BUTTON_LINK));
        }
    }

    public void gotoProjectEdit() {
        breadcrumb.select(0);
        breadcrumb.addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        MyCollabUI.addFragment("project/edit/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(ProjectI18nEnum.SINGLE), project.getName()));
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
