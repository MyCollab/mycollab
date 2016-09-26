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
package com.mycollab.module.project.view.bug.components;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.bug.IPrioritySummaryChartWidget;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.ButtonI18nComp;
import com.mycollab.vaadin.web.ui.DepotWithChart;
import com.mycollab.vaadin.web.ui.ProgressBarIndicator;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UnresolvedBugsByPriorityWidget extends DepotWithChart {
    private static final long serialVersionUID = 1L;

    private BugSearchCriteria searchCriteria;
    private int totalCount;
    private List<GroupItem> groupItems;

    public void setSearchCriteria(final BugSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;

        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
        totalCount = bugService.getTotalCount(searchCriteria);
        this.setTitle(UserUIContext.getMessage(BugI18nEnum.WIDGET_UNRESOLVED_BY_PRIORITY_TITLE) + " (" + totalCount + ")");
        groupItems = bugService.getPrioritySummary(searchCriteria);
        displayPlainMode();
    }

    @Override
    protected void displayChartMode() {
        bodyContent.removeAllComponents();
        IPrioritySummaryChartWidget prioritySummaryChartWidget = ViewManager.getCacheComponent(IPrioritySummaryChartWidget.class);
        prioritySummaryChartWidget.displayChart(searchCriteria);
        bodyContent.addComponent(prioritySummaryChartWidget);
    }

    @Override
    protected void displayPlainMode() {
        bodyContent.removeAllComponents();
        BugPriorityClickListener listener = new BugPriorityClickListener();
        if (!groupItems.isEmpty()) {
            for (OptionI18nEnum.Priority priority : OptionI18nEnum.task_priorities) {
                boolean isFound = false;
                for (GroupItem item : groupItems) {
                    if (priority.name().equals(item.getGroupid())) {
                        isFound = true;
                        MHorizontalLayout priorityLayout = new MHorizontalLayout().withFullWidth();
                        priorityLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                        MButton priorityLink = new ButtonI18nComp(priority.name(), priority, listener)
                                .withIcon(ProjectAssetsManager.getPriority(priority.name()))
                                .withStyleName(WebUIConstants.BUTTON_LINK, "bug-" + priority.name().toLowerCase())
                                .withWidth("110px");

                        ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount - item.getValue().intValue(), false);
                        indicator.setWidth("100%");

                        priorityLayout.with(priorityLink, indicator).expand(indicator);
                        bodyContent.addComponent(priorityLayout);
                    }
                }

                if (!isFound) {
                    MHorizontalLayout priorityLayout = new MHorizontalLayout().withFullWidth();
                    priorityLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                    Button priorityLink = new ButtonI18nComp(priority.name(), priority, listener);
                    priorityLink.setIcon(ProjectAssetsManager.getPriority(priority.name()));
                    priorityLink.setWidth("110px");
                    priorityLink.setStyleName(WebUIConstants.BUTTON_LINK);
                    priorityLink.addStyleName("bug-" + priority.name().toLowerCase());
                    ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount, false);
                    indicator.setWidth("100%");
                    priorityLayout.with(priorityLink, indicator).expand(indicator);
                    bodyContent.addComponent(priorityLayout);
                }
            }
        }
    }

    private class BugPriorityClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(ClickEvent clickEvent) {
            String key = ((ButtonI18nComp) clickEvent.getButton()).getKey();
            searchCriteria.setPriorities(new SetSearchField<>(key));
            EventBusFactory.getInstance().post(new BugEvent.SearchRequest(this, searchCriteria));
        }
    }
}
