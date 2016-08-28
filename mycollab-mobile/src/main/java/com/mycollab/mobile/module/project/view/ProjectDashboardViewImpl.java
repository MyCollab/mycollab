/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view;

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.*;
import com.mycollab.mobile.module.project.ui.ProjectMobileMenuPageView;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class ProjectDashboardViewImpl extends ProjectMobileMenuPageView implements ProjectDashboardView {
    private static final long serialVersionUID = 2364544271302929730L;

    private final CssLayout mainLayout;

    public ProjectDashboardViewImpl() {
        super();
        this.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));
        this.setRightComponent(buildRightComponent());
        mainLayout = new CssLayout();
        mainLayout.setSizeFull();
        mainLayout.setStyleName("project-dashboard");
        this.setContent(mainLayout);
    }

    private Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");

        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(AppContext.getMessage(MessageI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new MessageEvent.GotoAdd(this, null))));

        content.with(new Button(AppContext.getMessage(MilestoneI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoAdd(this, null))));

        content.with(new Button(AppContext.getMessage(TaskI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(this, null))));

        content.with(new Button(AppContext.getMessage(BugI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null))));

        content.with(new Button(AppContext.getMessage(RiskI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new RiskEvent.GotoAdd(this, null))));

        menu.setContent(content);
        return menu;
    }

    @Override
    public void displayDashboard() {
        mainLayout.removeAllComponents();
        SimpleProject currentProject = CurrentProjectVariables.getProject();
        VerticalLayout projectInfo = new VerticalLayout();
        projectInfo.setStyleName("project-info-layout");
        projectInfo.setWidth("100%");
        projectInfo.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        ELabel projectIcon = ELabel.fontIcon(FontAwesome.BUILDING_O).withStyleName("project-icon").withWidthUndefined();
        projectInfo.addComponent(projectIcon);

        ELabel projectName = new ELabel(currentProject.getName()).withFullWidth().withStyleName("project-name");
        projectInfo.addComponent(projectName);

        MHorizontalLayout metaInfo = new MHorizontalLayout();

        Label projectMemberBtn = ELabel.html(FontAwesome.USERS.getHtml() + " " + currentProject.getNumActiveMembers())
                .withDescription(AppContext.getMessage(ProjectMemberI18nEnum.OPT_ACTIVE_MEMBERS)).withStyleName(UIConstants.META_INFO);

        metaInfo.addComponent(projectMemberBtn);
        Label createdTimeLbl = ELabel.html(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime
                (currentProject.getCreatedtime())).withDescription(AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME))
                .withStyleName(UIConstants.META_INFO);
        metaInfo.addComponent(createdTimeLbl);

        Label billableHoursLbl = ELabel.html(FontAwesome.MONEY.getHtml() + " " + NumberUtils.roundDouble(2, currentProject.getTotalBillableHours()))
                .withDescription(AppContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)).withStyleName(UIConstants.META_INFO);
        metaInfo.addComponent(billableHoursLbl);

        Label nonBillableHoursLbl = ELabel.html(FontAwesome.GIFT.getHtml() + " " + NumberUtils.roundDouble(2,
                currentProject.getTotalNonBillableHours()))
                .withDescription(AppContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)).withStyleName(UIConstants.META_INFO);
        metaInfo.addComponent(nonBillableHoursLbl);
        projectInfo.addComponent(metaInfo);

        int openAssignments = currentProject.getNumOpenBugs() + currentProject.getNumOpenTasks() + currentProject.getNumOpenRisks() + currentProject.getNumOpenRisks();
        int totalAssignments = currentProject.getNumBugs() + currentProject.getNumTasks() + currentProject.getNumRisks();
        ELabel progressInfoLbl;
        if (totalAssignments > 0) {
            progressInfoLbl = new ELabel(AppContext.getMessage(ProjectI18nEnum.OPT_PROJECT_ASSIGNMENT,
                    (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                            * 100 / totalAssignments)).withWidthUndefined().withStyleName(UIConstants.META_INFO);
        } else {
            progressInfoLbl = new ELabel(AppContext.getMessage(ProjectI18nEnum.OPT_NO_ASSIGNMENT)).withWidthUndefined().withStyleName
                    (UIConstants.META_INFO);
        }
        projectInfo.addComponent(progressInfoLbl);

        mainLayout.addComponent(projectInfo);

        VerticalComponentGroup btnGroup = new VerticalComponentGroup();

        NavigationButton activityBtn = new NavigationButton("Activities");
        activityBtn.addClickListener(navigationButtonClickEvent -> EventBusFactory.getInstance().post(
                new ProjectEvent.MyProjectActivities(this, CurrentProjectVariables.getProjectId())));
        btnGroup.addComponent(new NavigationButtonWrap(FontAwesome.INBOX, activityBtn));

        NavigationButton messageBtn = new NavigationButton(AppContext.getMessage(MessageI18nEnum.LIST));
        messageBtn.addClickListener(navigationButtonClickEvent -> EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null)));
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE), messageBtn));

        NavigationButton milestoneBtn = new NavigationButton(AppContext.getMessage(MilestoneI18nEnum.LIST));
        milestoneBtn.addClickListener(navigationButtonClickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null)));
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE), milestoneBtn));

        NavigationButton taskBtn = new NavigationButton(AppContext.getMessage(TaskI18nEnum.LIST));
        taskBtn.addClickListener(navigationButtonClickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoList(this, null)));
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK), taskBtn));

        NavigationButton bugBtn = new NavigationButton(AppContext.getMessage(BugI18nEnum.LIST));
        bugBtn.addClickListener(navigationButtonClickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null)));
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG), bugBtn));

        NavigationButton riskBtn = new NavigationButton(AppContext.getMessage(RiskI18nEnum.LIST));
        riskBtn.addClickListener(navigationButtonClickEvent -> EventBusFactory.getInstance().post(new RiskEvent.GotoList(this, null)));
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK), riskBtn));

        NavigationButton userBtn = new NavigationButton(AppContext.getMessage(ProjectMemberI18nEnum.LIST));
        userBtn.addClickListener(navigationButtonClickEvent -> EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null)));
        btnGroup.addComponent(new NavigationButtonWrap(FontAwesome.USERS, userBtn));

        mainLayout.addComponent(btnGroup);
    }

    private static class NavigationButtonWrap extends MHorizontalLayout {
        NavigationButtonWrap(FontAwesome icon, NavigationButton button) {
            this.setStyleName("navigation-button-wrap");
            ELabel iconLbl = ELabel.fontIcon(icon).withWidthUndefined();
            with(iconLbl, button).withAlign(iconLbl, Alignment.MIDDLE_LEFT).expand(button);
        }
    }
}
