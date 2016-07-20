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
package com.mycollab.module.project.view.task.components;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.domain.GroupItem;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.events.TaskEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.task.ITaskPriorityChartWidget;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.ButtonI18nComp;
import com.mycollab.vaadin.web.ui.DepotWithChart;
import com.mycollab.vaadin.web.ui.ProgressBarIndicator;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class UnresolvedTaskByPriorityWidget extends DepotWithChart {
    private static final long serialVersionUID = 1L;

    private TaskSearchCriteria searchCriteria;
    private int totalCount;
    private List<GroupItem> groupItems;

    private ApplicationEventListener<TaskEvent.HasTaskChange> taskChangeHandler = new ApplicationEventListener<TaskEvent.HasTaskChange>() {
        @Override
        @Subscribe
        public void handle(TaskEvent.HasTaskChange event) {
            if (searchCriteria != null) {
                UI.getCurrent().access(() -> setSearchCriteria(searchCriteria));
            }
        }
    };

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(taskChangeHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(taskChangeHandler);
        super.detach();
    }

    public void setSearchCriteria(TaskSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;

        ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
        totalCount = taskService.getTotalCount(searchCriteria);
        groupItems = taskService.getPrioritySummary(searchCriteria);
        displayPlainMode();
    }

    @Override
    protected void displayPlainMode() {
        this.bodyContent.removeAllComponents();
        TaskPriorityClickListener listener = new TaskPriorityClickListener();
        this.setTitle(AppContext.getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_PRIORITY_TITLE) + " (" + totalCount + ")");

        if (!groupItems.isEmpty()) {
            for (TaskPriority priority : OptionI18nEnum.task_priorities) {
                boolean isFound = false;
                for (GroupItem item : groupItems) {
                    if (priority.name().equals(item.getGroupid())) {
                        isFound = true;
                        MHorizontalLayout priorityLayout = new MHorizontalLayout().withFullWidth();
                        priorityLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                        MButton priorityLink = new ButtonI18nComp(priority.name(), priority, listener)
                                .withIcon(ProjectAssetsManager.getTaskPriority(priority.name()))
                                .withStyleName(UIConstants.BUTTON_LINK, "task-" + priority.name().toLowerCase())
                                .withWidth("110px");

                        priorityLayout.addComponent(priorityLink);
                        ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount,
                                totalCount - item.getValue().intValue(), false);
                        indicator.setWidth("100%");
                        priorityLayout.with(indicator).expand(indicator);

                        this.bodyContent.addComponent(priorityLayout);
                    }
                }

                if (!isFound) {
                    MHorizontalLayout priorityLayout = new MHorizontalLayout().withFullWidth();
                    priorityLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                    MButton priorityLink = new ButtonI18nComp(priority.name(), priority, listener)
                            .withIcon(ProjectAssetsManager.getTaskPriority(priority.name()))
                            .withStyleName(UIConstants.BUTTON_LINK, "task-" + priority.name().toLowerCase())
                            .withWidth("100px");
                    priorityLayout.addComponent(priorityLink);
                    ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount, false);
                    indicator.setWidth("100%");
                    priorityLayout.with(indicator).expand(indicator);
                    this.bodyContent.addComponent(priorityLayout);
                }
            }
        }
    }

    @Override
    protected void displayChartMode() {
        this.bodyContent.removeAllComponents();
        ITaskPriorityChartWidget taskPriorityChartWidget = ViewManager.getCacheComponent(ITaskPriorityChartWidget.class);
        taskPriorityChartWidget.displayChart(searchCriteria);
        bodyContent.addComponent(taskPriorityChartWidget);
    }

    private class TaskPriorityClickListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(final ClickEvent event) {
            String key = ((ButtonI18nComp) event.getButton()).getKey();
            TaskSearchCriteria criteria = BeanUtility.deepClone(searchCriteria);
            criteria.setPriorities(new SetSearchField<>(key));
            EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(this, criteria));
        }
    }
}