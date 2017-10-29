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

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.file.PathUtils;
import com.mycollab.module.page.domain.Folder;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.page.service.PageService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.domain.*;
import com.mycollab.module.project.event.*;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.CommonUIFactory;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.utils.LabelStringGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectBreadcrumb extends MHorizontalLayout implements CacheableComponent {
    private static final long serialVersionUID = 1L;
    private static LabelStringGenerator menuLinkGenerator = new BreadcrumbLabelStringGenerator();

    private SimpleProject project;

    public ProjectBreadcrumb() {
        setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
    }

    public void setProject(SimpleProject project) {
        this.project = project;
    }

    private void addSummaryLink() {
        removeAllComponents();
        with(ELabel.h3(new A(ProjectLinkGenerator.generateProjectLink(project.getId())).appendText(StringUtils
                .trim(project.getName(), 30, true)).write())).withDescription(ProjectTooltipGenerator.generateToolTipProject(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                project, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
    }

    private void addLink(Button button) {
        button.addStyleName(WebThemes.BUTTON_LINK);
        with(new ELabel("/"), button);
    }

    private void addEnabledLink(Button button) {
        button.addStyleName(WebThemes.BUTTON_LINK);
        with(new ELabel("/"), button);
    }

    public void gotoSearchProjectItems() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH)));
        AppUI.addFragment(ProjectLinkGenerator.generateProjectLink(project.getId()),
                UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
    }

    public void gotoMessageList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(MessageI18nEnum.LIST)));
        AppUI.addFragment(ProjectLinkGenerator.generateMessagesLink(project.getId()),
                UserUIContext.getMessage(MessageI18nEnum.LIST));
    }

    public void gotoMessage(Message message) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(MessageI18nEnum.LIST),
                clickEvent -> EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null))));
        addLink(generateBreadcrumbLink(message.getTitle()));
        AppUI.addFragment(ProjectLinkGenerator.generateMessagePreviewLink(project.getId(), message.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(MessageI18nEnum.SINGLE), message.getTitle()));
    }

    public void gotoRiskRead(Risk risk) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));
        addLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(RiskI18nEnum.SINGLE), risk.getName())));
        AppUI.addFragment(ProjectLinkGenerator.generateRiskPreviewLink(project.getId(), risk.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(RiskI18nEnum.SINGLE), risk.getName()));
    }

    public void gotoRiskEdit(final Risk risk) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));
        addLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(RiskI18nEnum.SINGLE), risk.getName()),
                clickEvent -> EventBusFactory.getInstance().post(new RiskEvent.GotoRead(this, risk.getId()))));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppUI.addFragment(ProjectLinkGenerator.generateRiskEditLink(project.getId(), risk.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(RiskI18nEnum.SINGLE), risk.getName()));
    }

    public void gotoRiskAdd() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppUI.addFragment(ProjectLinkGenerator.generateRiskAddLink(project.getId()),
                UserUIContext.getMessage(RiskI18nEnum.NEW));
    }

    public void gotoMilestoneList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST)));
        AppUI.addFragment(ProjectLinkGenerator.generateMilestonesLink(project.getId()),
                UserUIContext.getMessage(MilestoneI18nEnum.LIST));
    }

    public void gotoRoadmap() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_ROADMAP)));
        AppUI.addFragment("project/roadmap/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_ROADMAP));
    }

    public void gotoMilestoneKanban() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN)));
        AppUI.addFragment("project/milestone/kanban/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN));
    }

    public void gotoMilestoneRead(Milestone milestone) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        addLink(generateBreadcrumbLink(milestone.getName()));
        AppUI.addFragment(ProjectLinkGenerator.generateMilestonePreviewLink(project.getId(), milestone.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), milestone.getName()));
    }

    public void gotoMilestoneEdit(final Milestone milestone) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        addLink(generateBreadcrumbLink(milestone.getName(),
                clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoRead(this, milestone.getId()))));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppUI.addFragment("project/milestone/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + milestone.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), milestone.getName()));
    }

    public void gotoMilestoneAdd() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.LIST), new GotoMilestoneListListener()));
        addLink(new Button(UserUIContext.getMessage(MilestoneI18nEnum.NEW)));
        AppUI.addFragment("project/milestone/add/" + UrlEncodeDecoder.encode(project.getId()),
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
        String basePath = PathUtils.getProjectDocumentPath(AppUI.getAccountId(), CurrentProjectVariables.getProjectId());
        String currentPath = CurrentProjectVariables.getCurrentPagePath();

        addEnabledLink(new Button(UserUIContext.getMessage(PageI18nEnum.LIST), new GotoPageListListener(basePath)));

        String extraPath = currentPath.substring(basePath.length());
        if (extraPath.startsWith("/")) {
            extraPath = extraPath.substring(1);
        }
        if (!extraPath.equals("")) {
            PageService pageService = AppContextUtil.getSpringBean(PageService.class);

            String[] subPath = extraPath.split("/");
            StringBuilder tempPath = new StringBuilder();
            for (String var : subPath) {
                tempPath.append("/").append(var);
                String folderPath = basePath + tempPath.toString();
                Folder folder = pageService.getFolder(folderPath);
                if (folder != null) {
                    addLink(new Button(folder.getName(), new GotoPageListListener(folderPath)));
                } else {
                    return;
                }
            }
        }
    }

    public void gotoPageList() {
        addSummaryLink();
        buildPageBreadcrumbChain();
        AppUI.addFragment(ProjectLinkGenerator.generatePagesLink(project.getId(),
                CurrentProjectVariables.getCurrentPagePath()), UserUIContext.getMessage(PageI18nEnum.LIST));
    }

    public void gotoPageAdd() {
        addSummaryLink();
        buildPageBreadcrumbChain();
        addLink(new Button(UserUIContext.getMessage(PageI18nEnum.NEW)));
        AppUI.addFragment(ProjectLinkGenerator.generatePageAdd(
                project.getId(), CurrentProjectVariables.getCurrentPagePath()),
                UserUIContext.getMessage(PageI18nEnum.NEW));
    }

    public void gotoPageRead(Page page) {
        addSummaryLink();
        buildPageBreadcrumbChain();
        addLink(new Button(StringUtils.trim(page.getSubject(), 50)));
        AppUI.addFragment(ProjectLinkGenerator.generatePageRead(project.getId(), page.getPath()),
                UserUIContext.getMessage(PageI18nEnum.DETAIL));
    }

    public void gotoPageEdit(Page page) {
        addSummaryLink();
        buildPageBreadcrumbChain();

        AppUI.addFragment(ProjectLinkGenerator.generatePageEdit(project.getId(), page.getPath()),
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

    public void gotoTicketDashboard(String query) {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST)));
        String fragment = (StringUtils.isNotBlank(query)) ? ProjectLinkGenerator.generateTicketDashboardLink(project.getId()) + "?" + query : ProjectLinkGenerator.generateTicketDashboardLink(project.getId());
        AppUI.addFragment(fragment, UserUIContext.getMessage(TicketI18nEnum.LIST));
    }

    public void gotoTicketKanbanView() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));
        addLink(new Button(String.format("%s: %s", UserUIContext.getMessage(TicketI18nEnum.SINGLE),
                UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN))));
        AppUI.addFragment("project/ticket/kanban/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN));
    }

    public void gotoTaskAdd() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));
        addLink(new Button(UserUIContext.getMessage(TaskI18nEnum.NEW)));
        AppUI.addFragment("project/task/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(TaskI18nEnum.NEW));
    }

    public void gotoGanttView() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_GANTT_CHART)));
        AppUI.addFragment("project/gantt/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_GANTT_CHART));
    }

    public void gotoCalendar() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_CALENDAR)));
        AppUI.addFragment("project/calendar/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_CALENDAR));
    }

    public void gotoTaskRead(SimpleTask task) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));
        addLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getName())));
        AppUI.addFragment(ProjectLinkGenerator.generateTaskPreviewLink(task.getTaskkey(), task.getProjectShortname()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getName()));
    }

    public void gotoTaskEdit(final SimpleTask task) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));
        addEnabledLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getName()),
                clickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, task.getId()))));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppUI.addFragment(ProjectLinkGenerator.generateTaskEditLink(task.getTaskkey(), task.getProjectShortname()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getName()));
    }

    private static class GotoTicketDashboard implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null));
        }
    }

    public void gotoBugAdd() {
        addSummaryLink();

        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));

        addLink(new Button(UserUIContext.getMessage(BugI18nEnum.NEW)));
        AppUI.addFragment("project/bug/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(BugI18nEnum.NEW));
    }

    public void gotoBugEdit(final SimpleBug bug) {
        addSummaryLink();

        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));

        addLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(BugI18nEnum.SINGLE), bug.getName()),
                clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, bug.getId()))));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppUI.addFragment(ProjectLinkGenerator.generateBugEditLink(bug.getBugkey(),
                bug.getProjectShortName()), UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                UserUIContext.getMessage(BugI18nEnum.SINGLE), bug.getName()));
    }

    public void gotoBugRead(SimpleBug bug) {
        addSummaryLink();

        addEnabledLink(new Button(UserUIContext.getMessage(TicketI18nEnum.LIST), new GotoTicketDashboard()));

        addLink(generateBreadcrumbLink(UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                UserUIContext.getMessage(BugI18nEnum.SINGLE), bug.getName())));
        AppUI.addFragment(ProjectLinkGenerator.generateBugPreviewLink(bug.getBugkey(), bug.getProjectShortName()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(BugI18nEnum.SINGLE), bug.getName()));
    }

    public void gotoVersionList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST)));
        AppUI.addFragment("project/version/list/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(VersionI18nEnum.LIST));
    }

    public void gotoVersionAdd() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppUI.addFragment("project/version/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(VersionI18nEnum.NEW));
    }

    public void gotoVersionEdit(final Version version) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        addLink(generateBreadcrumbLink(version.getName(),
                clickEvent -> EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId()))));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppUI.addFragment("project/version/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + version.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(VersionI18nEnum.SINGLE), version.getName()));
    }

    public void gotoVersionRead(Version version) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(VersionI18nEnum.LIST), new GotoVersionListener()));
        addLink(generateBreadcrumbLink(version.getName()));
        AppUI.addFragment(ProjectLinkGenerator.generateBugVersionPreviewLink(project.getId(), version.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(VersionI18nEnum.SINGLE), version.getName()));
    }

    private static class GotoVersionListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
        }
    }

    public void gotoTagList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG)));
        AppUI.addFragment("project/tag/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG));
    }

    public void gotoFavoriteList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES)));
        AppUI.addFragment("project/favorite/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES));
    }

    public void gotoComponentList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST)));
        AppUI.addFragment("project/component/list/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ComponentI18nEnum.LIST));
    }

    public void gotoComponentAdd() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppUI.addFragment("project/component/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ComponentI18nEnum.NEW));
    }

    public void gotoComponentEdit(final Component component) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        addLink(generateBreadcrumbLink(component.getName(),
                clickEvent -> EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, component.getId()))));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppUI.addFragment("project/component/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + component.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                        UserUIContext.getMessage(ComponentI18nEnum.SINGLE), component.getName()));
    }

    public void gotoComponentRead(Component component) {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ComponentI18nEnum.LIST), new GotoComponentListener()));
        addLink(generateBreadcrumbLink(component.getName()));
        AppUI.addFragment(ProjectLinkGenerator.generateBugComponentPreviewLink(project.getId(), component.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(ComponentI18nEnum.SINGLE), component.getName()));
    }

    public void gotoTimeTrackingList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TIME)));
        AppUI.addFragment(ProjectLinkGenerator.generateTimeReportLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_TIME_TRACKING));
    }

    public void gotoInvoiceView() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(InvoiceI18nEnum.LIST)));
        AppUI.addFragment(ProjectLinkGenerator.generateInvoiceListLink(project.getId()),
                UserUIContext.getMessage(InvoiceI18nEnum.LIST));
    }

    public void gotoFileList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FILE)));
        AppUI.addFragment(ProjectLinkGenerator.generateFileDashboardLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_FILES));
    }

    public void gotoUserList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST)));
        AppUI.addFragment(ProjectLinkGenerator.generateUsersLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBERS));
    }

    public void gotoUserAdd() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        addLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES)));
        AppUI.addFragment("project/user/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_INVITE_MEMBERS));
    }

    public void gotoUserRead(SimpleProjectMember member) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        addLink(generateBreadcrumbLink(member.getMemberFullName()));
        AppUI.addFragment("project/user/preview/" + UrlEncodeDecoder.encode(project.getId() + "/"
                + member.getUsername()), UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBER_READ, member.getMemberFullName()));
    }

    public void gotoUserEdit(SimpleProjectMember member) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), new GotoUserListener()));
        addLink(generateBreadcrumbLink(member.getMemberFullName()));
        AppUI.addFragment("project/user/edit/" + UrlEncodeDecoder.encode(project.getId() + "/"
                + member.getId()), UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBER_EDIT, member.getMemberFullName()));
    }

    public void gotoRoleList() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST)));
        AppUI.addFragment("project/role/list/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectRoleI18nEnum.LIST));
    }

    public void gotoRoleRead(SimpleProjectRole role) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoRoleListener()));
        addLink(generateBreadcrumbLink(role.getRolename()));
        AppUI.addFragment("project/role/preview/" + UrlEncodeDecoder.encode(project.getId() + "/" + role.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(ProjectRoleI18nEnum.SINGLE), role.getRolename()));
    }

    public void gotoProjectSetting() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_SETTING), new GotoNotificationSettingListener()));
        AppUI.addFragment(ProjectLinkGenerator.generateProjectSettingLink(project.getId()),
                UserUIContext.getMessage(BreadcrumbI18nEnum.FRA_SETTING));
    }

    public void gotoRoleAdd() {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoRoleListener()));
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppUI.addFragment("project/role/add/" + UrlEncodeDecoder.encode(project.getId()),
                UserUIContext.getMessage(ProjectRoleI18nEnum.NEW));
    }

    public void gotoRoleEdit(ProjectRole role) {
        addSummaryLink();
        addEnabledLink(new Button(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST), new GotoUserListener()));
        addLink(generateBreadcrumbLink(role.getRolename()));
        AppUI.addFragment("project/role/edit/" + UrlEncodeDecoder.encode(project.getId() + "/" + role.getId()),
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
            EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, CurrentProjectVariables.getProjectId()));
        }
    }

    private static class GotoComponentListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new BugComponentEvent.GotoList(this, null));
        }
    }

    public void gotoProjectDashboard() {
        addSummaryLink();
        AppUI.addFragment(ProjectLinkGenerator.generateProjectLink(project.getId()),
                UserUIContext.getMessage(GenericI18Enum.VIEW_DASHBOARD));
//        if (CurrentProjectVariables.isAdmin() && !SiteConfiguration.isCommunityEdition()) {
//            buildCustomizeDashboardView();
//        }
    }

    public void gotoProjectEdit() {
        addSummaryLink();
        addLink(new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppUI.addFragment("project/edit/" + UrlEncodeDecoder.encode(project.getId()),
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
