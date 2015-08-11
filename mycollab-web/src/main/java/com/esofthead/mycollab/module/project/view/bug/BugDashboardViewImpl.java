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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.module.project.view.bug.components.*;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
@ViewComponent
public class BugDashboardViewImpl extends AbstractLazyPageView implements BugDashboardView {
    private MVerticalLayout leftColumn, rightColumn;
    private MHorizontalLayout header;

    private ApplicationEventListener<BugEvent.SearchRequest> searchHandler = new
            ApplicationEventListener<BugEvent.SearchRequest>() {
                @Override
                @Subscribe
                public void handle(BugEvent.SearchRequest event) {
                    BugSearchCriteria criteria = (BugSearchCriteria) event.getData();
                    if (criteria != null) {
                        EventBusFactory.getInstance().post(new BugEvent.GotoList(BugDashboardViewImpl.class, criteria));
                    }
                }
            };

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(searchHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(searchHandler);
        super.detach();
    }

    private void initUI() {
        this.setMargin(new MarginInfo(false, true, false, true));
        header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false)).withWidth("100%");
        header.addStyleName("hdr-view");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(header);

        MHorizontalLayout body = new MHorizontalLayout().withWidth("100%");

        leftColumn = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, false));
        rightColumn = new MVerticalLayout().withMargin(false);

        body.with(leftColumn, rightColumn).withAlign(rightColumn, Alignment.TOP_RIGHT).expand(leftColumn);

        addComponent(body);

        initHeader();
    }

    private void initHeader() {
        ProjectViewHeader title = new ProjectViewHeader(ProjectTypeConstants.BUG,
                AppContext.getMessage(BugI18nEnum.VIEW_BUG_DASHBOARD_TITLE));
        header.with(title).withAlign(title, Alignment.MIDDLE_LEFT).expand(title);

        Button createBugBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG), new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
            }
        });
        createBugBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.BUGS));
        createBugBtn.setIcon(FontAwesome.PLUS);
        final SplitButton controlsBtn = new SplitButton(createBugBtn);
        controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        controlsBtn.setWidthUndefined();

        OptionPopupContent btnControlsLayout = new OptionPopupContent().withWidth("180px");
        Button createComponentBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_COMPONENT), new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                controlsBtn.setPopupVisible(false);
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoAdd(this, null));
            }
        });
        createComponentBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.COMPONENTS));
        createComponentBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_COMPONENT));
        btnControlsLayout.addOption(createComponentBtn);

        Button createVersionBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_VERSION), new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                controlsBtn.setPopupVisible(false);
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoAdd(this, null));
            }
        });
        createVersionBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS));
        createVersionBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_VERSION));
        btnControlsLayout.addOption(createVersionBtn);

        controlsBtn.setContent(btnControlsLayout);
        header.addComponent(controlsBtn);
    }

    @Override
    protected void displayView() {
        initUI();

        rightColumn.setWidth("400px");
        MyBugListWidget myBugListWidget = new MyBugListWidget();
        leftColumn.addComponent(myBugListWidget);
        BugSearchCriteria myBugsSearchCriteria = new BugSearchCriteria();
        myBugsSearchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        myBugsSearchCriteria.setStatuses(new SetSearchField<>(BugStatus.InProgress.name(), BugStatus.Open.name(),
                BugStatus.ReOpened.name()));
        myBugsSearchCriteria.setAssignuser(new StringSearchField(AppContext.getUsername()));
        myBugListWidget.setSearchCriteria(myBugsSearchCriteria);

        DueBugWidget dueBugWidget = new DueBugWidget();
        leftColumn.addComponent(dueBugWidget);
        BugSearchCriteria dueDefectsCriteria = new BugSearchCriteria();
        dueDefectsCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        dueDefectsCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS(), DateTimeSearchField.LESSTHANEQUAL));
        dueDefectsCriteria.setStatuses(new SetSearchField<>(BugStatus.InProgress.name(),
                BugStatus.Open.name(), BugStatus.ReOpened.name(), BugStatus.Resolved.name()));
        dueBugWidget.setSearchCriteria(dueDefectsCriteria);

        BugSearchCriteria waitingApprovalCriteria = new BugSearchCriteria();
        waitingApprovalCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        waitingApprovalCriteria.setStatuses(new SetSearchField<>(new String[]{BugStatus.Resolved.name()}));
        waitingApprovalCriteria.setResolutions(new SetSearchField<>(new String[]{OptionI18nEnum.BugResolution.Fixed.name()}));
        BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
        int totalWaitingCount = bugService.getTotalCount(waitingApprovalCriteria);
        if (totalWaitingCount > 0) {
            WaitingApprovalBugsWidget waitingBugWidget = new WaitingApprovalBugsWidget();
            waitingBugWidget.setSearchCriteria(waitingApprovalCriteria);
            leftColumn.addComponent(waitingBugWidget);
        }

        RecentBugUpdateWidget updateBugWidget = new RecentBugUpdateWidget();
        BugSearchCriteria recentDefectsCriteria = new BugSearchCriteria();
        recentDefectsCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        updateBugWidget.setSearchCriteria(recentDefectsCriteria);
        leftColumn.addComponent(updateBugWidget);

        // Unresolved by assignee
        UnresolvedBugsByAssigneeWidget2 unresolvedByAssigneeWidget = new UnresolvedBugsByAssigneeWidget2();
        BugSearchCriteria unresolvedByAssigneeSearchCriteria = new BugSearchCriteria();
        unresolvedByAssigneeSearchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        unresolvedByAssigneeSearchCriteria.setStatuses(new SetSearchField<>(BugStatus.InProgress.name(),
                BugStatus.Open.name(), BugStatus.ReOpened.name()));
        unresolvedByAssigneeWidget.setSearchCriteria(unresolvedByAssigneeSearchCriteria);
        rightColumn.addComponent(unresolvedByAssigneeWidget);

        // Unresolve by priority widget
        UnresolvedBugsByPriorityWidget2 unresolvedByPriorityWidget = new UnresolvedBugsByPriorityWidget2();
        BugSearchCriteria unresolvedByPrioritySearchCriteria = new BugSearchCriteria();
        unresolvedByPrioritySearchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        unresolvedByPrioritySearchCriteria.setStatuses(new SetSearchField<>(BugStatus.InProgress.name(),
                BugStatus.Open.name(), BugStatus.ReOpened.name()));
        unresolvedByPriorityWidget.setSearchCriteria(unresolvedByPrioritySearchCriteria);
        rightColumn.addComponent(unresolvedByPriorityWidget);

        // bug chart
        BugSearchCriteria chartSearchCriteria = new BugSearchCriteria();
        chartSearchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        BugChartComponent bugChartComponent = new BugChartComponent(chartSearchCriteria, 400, 200);
        rightColumn.addComponent(bugChartComponent);
    }
}
