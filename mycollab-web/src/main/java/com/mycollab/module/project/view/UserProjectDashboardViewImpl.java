/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.jarektoro.responsivelayout.ResponsiveColumn;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.view.milestone.AllMilestoneTimelineWidget;
import com.mycollab.module.project.view.ticket.TicketOverdueWidget;
import com.mycollab.module.project.view.user.ActivityStreamComponent;
import com.mycollab.module.project.view.user.UserUnresolvedTicketWidget;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
@ViewComponent
public class UserProjectDashboardViewImpl extends AbstractVerticalPageView implements UserProjectDashboardView {

    public UserProjectDashboardViewImpl() {
        withMargin(true);
    }

    @Override
    public void lazyLoadView() {
        removeAllComponents();
        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        List<Integer> prjKeys = projectService.getOpenProjectKeysUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        if (CollectionUtils.isNotEmpty(prjKeys)) {
            ResponsiveLayout contentWrapper = new ResponsiveLayout(ResponsiveLayout.ContainerType.FIXED);
            contentWrapper.setSizeFull();
            addComponent(contentWrapper);

            ResponsiveRow row = new ResponsiveRow();

            AllMilestoneTimelineWidget milestoneTimelineWidget = new AllMilestoneTimelineWidget();
            TicketOverdueWidget ticketOverdueWidget = new TicketOverdueWidget();
            ActivityStreamComponent activityStreamComponent = new ActivityStreamComponent();
            UserUnresolvedTicketWidget unresolvedAssignmentThisWeekWidget = new UserUnresolvedTicketWidget();
            UserUnresolvedTicketWidget unresolvedAssignmentNextWeekWidget = new UserUnresolvedTicketWidget();

            ResponsiveColumn column1 = new ResponsiveColumn(12, 12, 6, 6);
            MVerticalLayout leftPanel = new MVerticalLayout(milestoneTimelineWidget,
                    unresolvedAssignmentThisWeekWidget, unresolvedAssignmentNextWeekWidget, ticketOverdueWidget)
                    .withMargin(new MarginInfo(true, true, false, false)).withFullWidth();
            column1.setComponent(leftPanel);

            ResponsiveColumn column2 = new ResponsiveColumn(12, 12, 6, 6);
            column2.setComponent(activityStreamComponent);

            row.addColumn(column1);
            row.addColumn(column2);
            contentWrapper.addRow(row);

            activityStreamComponent.showFeeds(prjKeys);
            milestoneTimelineWidget.display(prjKeys);
            ticketOverdueWidget.showTicketsByStatus(prjKeys);
            unresolvedAssignmentThisWeekWidget.displayUnresolvedAssignmentsThisWeek(prjKeys);
            unresolvedAssignmentNextWeekWidget.displayUnresolvedAssignmentsNextWeek(prjKeys);
        } else {
            this.with(ELabel.h1(VaadinIcons.TASKS.getHtml()).withUndefinedWidth());
            this.with(ELabel.h2(UserUIContext.getMessage(GenericI18Enum.VIEW_NO_ITEM_TITLE)).withUndefinedWidth());
            if (UserUIContext.canWrite(RolePermissionCollections.CREATE_NEW_PROJECT)) {
                MButton newProjectBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.NEW),
                        clickEvent -> UI.getCurrent().addWindow(ViewManager.getCacheComponent(AbstractProjectAddWindow.class)))
                        .withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.PLUS);
                with(newProjectBtn);
            }
            alignAll(Alignment.TOP_CENTER);
        }
    }
}
