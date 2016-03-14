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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.*;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectMobileMenuPageView;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.*;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
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
        content.with(new Button(AppContext.getMessage(MessageI18nEnum.BUTTON_NEW_MESSAGE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new MessageEvent.GotoAdd(this, null));
            }
        }));
        content.with(new Button(AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoAdd(this, null));
            }
        }));

        content.with(new Button(AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(this, null));
            }
        }));

        content.with(new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
            }
        }));

        content.with(new Button(AppContext.getMessage(RiskI18nEnum.BUTTON_NEW_RISK), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new RiskEvent.GotoAdd(this, null));
            }
        }));

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

        ELabel projectIcon = ELabel.h2(FontAwesome.BUILDING_O.getHtml()).withStyleName("project-icon").withWidthUndefined();
        projectInfo.addComponent(projectIcon);

        ELabel projectName = new ELabel(currentProject.getName()).withWidth("100%").withStyleName("project-name");
        projectInfo.addComponent(projectName);

        MHorizontalLayout metaInfo = new MHorizontalLayout();

        Label projectMemberBtn = new ELabel(FontAwesome.USERS.getHtml() + " " + currentProject.getNumActiveMembers(),
                ContentMode.HTML).withDescription("Active members").withStyleName(UIConstants.META_INFO);

        metaInfo.addComponent(projectMemberBtn);
        Label createdTimeLbl = new ELabel(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime(currentProject.getCreatedtime()),
                ContentMode.HTML).withDescription("Created time").withStyleName(UIConstants.META_INFO);
        metaInfo.addComponent(createdTimeLbl);

        Label billableHoursLbl = new ELabel(FontAwesome.MONEY.getHtml() + " " + NumberUtils.roundDouble(2, currentProject.getTotalBillableHours()),
                ContentMode.HTML).withDescription("Billable hours").withStyleName(UIConstants.META_INFO);
        metaInfo.addComponent(billableHoursLbl);

        Label nonBillableHoursLbl = new ELabel(FontAwesome.GIFT.getHtml() + " " + NumberUtils.roundDouble(2,
                currentProject.getTotalNonBillableHours()), ContentMode.HTML)
                .withDescription("Non billable hours").withStyleName(UIConstants.META_INFO);
        metaInfo.addComponent(nonBillableHoursLbl);
        projectInfo.addComponent(metaInfo);

        int openAssignments = currentProject.getNumOpenBugs() + currentProject.getNumOpenTasks() + currentProject.getNumOpenRisks() + currentProject.getNumOpenRisks();
        int totalAssignments = currentProject.getNumBugs() + currentProject.getNumTasks() + currentProject.getNumRisks();
        ELabel progressInfoLbl;
        if (totalAssignments > 0) {
            progressInfoLbl = new ELabel(String.format("%d of %d issue(s) resolved. Progress (%d%%)",
                    (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                            * 100 / totalAssignments)).withWidthUndefined().withStyleName(UIConstants.META_INFO);
        } else {
            progressInfoLbl = new ELabel("No issue").withWidthUndefined().withStyleName(UIConstants.META_INFO);
        }
        projectInfo.addComponent(progressInfoLbl);

        mainLayout.addComponent(projectInfo);

        VerticalComponentGroup btnGroup = new VerticalComponentGroup();

        NavigationButton activityBtn = new NavigationButton("Activities");
        activityBtn.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent navigationButtonClickEvent) {
                EventBusFactory.getInstance().post(new ProjectEvent.MyProjectActivities(this, CurrentProjectVariables.getProjectId()));
            }
        });
        btnGroup.addComponent(new NavigationButtonWrap(FontAwesome.INBOX, activityBtn));

        NavigationButton messageBtn = new NavigationButton(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE));
        messageBtn.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
            }
        });
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE), messageBtn));

        NavigationButton milestoneBtn = new NavigationButton(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE));
        milestoneBtn.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
            }
        });
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE), milestoneBtn));

        NavigationButton taskBtn = new NavigationButton(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_TASK));
        taskBtn.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoList(this, null));
            }
        });
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK), taskBtn));

        NavigationButton bugBtn = new NavigationButton(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_BUG));
        bugBtn.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null));
            }
        });
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG), bugBtn));

        NavigationButton riskBtn = new NavigationButton(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_RISK));
        riskBtn.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                EventBusFactory.getInstance().post(new RiskEvent.GotoList(this, null));
            }
        });
        btnGroup.addComponent(new NavigationButtonWrap(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK), riskBtn));

        NavigationButton userBtn = new NavigationButton(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_USERS));
        userBtn.addClickListener(new NavigationButton.NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
            }
        });
        btnGroup.addComponent(new NavigationButtonWrap(FontAwesome.USERS, userBtn));

        mainLayout.addComponent(btnGroup);
    }

    private static class NavigationButtonWrap extends MHorizontalLayout {
        NavigationButtonWrap(FontAwesome icon, NavigationButton button) {
            this.setStyleName("navigation-button-wrap");
            ELabel iconLbl = new ELabel(icon.getHtml(), ContentMode.HTML).withWidthUndefined();
            with(iconLbl, button).withAlign(iconLbl, Alignment.MIDDLE_LEFT).expand(button);
        }
    }
}
