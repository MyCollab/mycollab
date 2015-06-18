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
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TaskGroupListDisplay extends
        DefaultPagedBeanList<ProjectTaskListService, TaskListSearchCriteria, SimpleTaskList> {
    private static final long serialVersionUID = -9048439116024747065L;

    public TaskGroupListDisplay() {
        super(ApplicationContextUtil.getSpringBean(ProjectTaskListService.class),
                new TaskGroupRowDisplayHandler());
        this.addStyleName("task-group-list");
    }

    private static class TaskGroupRowDisplayHandler implements RowDisplayHandler<SimpleTaskList> {

        @Override
        public Component generateRow(final SimpleTaskList taskList, int rowIndex) {
            HorizontalLayout taskListLayout = new HorizontalLayout();
            taskListLayout.setStyleName("task-list-layout");
            taskListLayout.setWidth("100%");
            taskListLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

                private static final long serialVersionUID = -3495723609008813660L;

                @Override
                public void layoutClick(
                        LayoutEvents.LayoutClickEvent event) {
                    EventBusFactory.getInstance().post(new TaskEvent.GoInsideList(this, taskList.getId()));
                }
            });

            VerticalLayout taskListInfo = new VerticalLayout();
            taskListInfo.setStyleName("task-list-info");
            Label b = new Label(taskList.getName());
            b.setWidth("100%");
            b.setStyleName("task-list-name");
            taskListInfo.addComponent(b);

            Label taskListUpdateTime = new Label(AppContext.getMessage(
                    DayI18nEnum.LAST_UPDATED_ON,
                    AppContext.formatDateTime(taskList.getLastupdatedtime())));
            taskListUpdateTime.setWidthUndefined();
            taskListUpdateTime.setStyleName("last-updated-time");
            taskListInfo.addComponent(taskListUpdateTime);
            taskListLayout.addComponent(taskListInfo);

            if (taskList.getNumOpenTasks() > 0) {
                Label activeTasksNum = new Label(taskList.getNumOpenTasks() + "");
                activeTasksNum.setWidthUndefined();
                activeTasksNum.setStyleName("active-task-num");
                taskListLayout.addComponent(activeTasksNum);
                taskListLayout.setComponentAlignment(activeTasksNum,
                        Alignment.MIDDLE_LEFT);
            }
            taskListLayout.setExpandRatio(taskListInfo, 1.0f);
            taskListLayout.addStyleName("list-item");

            return taskListLayout;
        }

    }

}
