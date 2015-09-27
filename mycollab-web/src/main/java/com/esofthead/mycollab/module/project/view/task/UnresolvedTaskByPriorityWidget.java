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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ButtonI18nComp;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.google.common.eventbus.Subscribe;
import com.rits.cloning.Cloner;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class UnresolvedTaskByPriorityWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private TaskSearchCriteria searchCriteria;
    private ApplicationEventListener<TaskEvent.HasTaskChange> taskChangeHandler = new
            ApplicationEventListener<TaskEvent.HasTaskChange>() {
                @Override
                @Subscribe
                public void handle(TaskEvent.HasTaskChange event) {
                    if (searchCriteria != null) {
                        UI.getCurrent().access(new Runnable() {
                            @Override
                            public void run() {
                                UnresolvedTaskByPriorityWidget.this.setSearchCriteria(searchCriteria);
                            }
                        });
                    }
                }
            };

    public UnresolvedTaskByPriorityWidget() {
        super(AppContext.getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_PRIORITY_TITLE), new MVerticalLayout());
        this.setMargin(new MarginInfo(false, false, true, false));
        this.setContentBorder(true);
    }

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
        this.bodyContent.removeAllComponents();
        ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
        int totalCount = taskService.getTotalCount(searchCriteria);
        List<GroupItem> groupItems = taskService.getPrioritySummary(searchCriteria);
        TaskPriorityClickListener listener = new TaskPriorityClickListener();
        this.setTitle(AppContext.getMessage(TaskI18nEnum.WIDGET_UNRESOLVED_BY_PRIORITY_TITLE) + " (" + totalCount + ")");

        if (!groupItems.isEmpty()) {
            for (TaskPriority priority : OptionI18nEnum.task_priorities) {
                boolean isFound = false;
                for (GroupItem item : groupItems) {
                    if (priority.name().equals(item.getGroupid())) {
                        isFound = true;
                        MHorizontalLayout priorityLayout = new MHorizontalLayout().withWidth("100%");
                        ButtonI18nComp priorityLink = new ButtonI18nComp(priority.name(), priority, listener);
                        priorityLink.setIcon(ProjectAssetsManager.getTaskPriority(priority.name()));
                        priorityLink.setWidth("110px");
                        priorityLink.setStyleName(UIConstants.THEME_LINK);
                        priorityLink.addStyleName("task-" + priority.name().toLowerCase());

                        priorityLayout.addComponent(priorityLink);
                        ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount - item.getValue(), false);
                        indicator.setWidth("100%");
                        priorityLayout.with(indicator).expand(indicator);

                        this.bodyContent.addComponent(priorityLayout);
                    }
                }

                if (!isFound) {
                    MHorizontalLayout priorityLayout = new MHorizontalLayout().withWidth("100%");
                    ButtonI18nComp priorityLink = new ButtonI18nComp(priority.name(), priority, listener);
                    priorityLink.setIcon(ProjectAssetsManager.getTaskPriority(priority.name()));
                    priorityLink.setWidth("110px");
                    priorityLink.setStyleName(UIConstants.THEME_LINK);
                    priorityLink.addStyleName("task-" + priority.name().toLowerCase());
                    priorityLayout.addComponent(priorityLink);
                    ProgressBarIndicator indicator = new ProgressBarIndicator(totalCount, totalCount, false);
                    indicator.setWidth("100%");
                    priorityLayout.with(indicator).expand(indicator);
                    this.bodyContent.addComponent(priorityLayout);
                }
            }
        }
    }

    private class TaskPriorityClickListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(final ClickEvent event) {
            String key = ((ButtonI18nComp) event.getButton()).getKey();
            Cloner cloner = new Cloner();
            TaskSearchCriteria criteria = cloner.deepClone(searchCriteria);
            criteria.setPriorities(new SetSearchField<>(key));
            EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(this, criteria));
        }
    }
}