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
package com.esofthead.mycollab.module.project.view.task.gantt;

import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.1.0
 */
public class TaskListGanttItemWrapper extends GanttItemWrapper {
    private ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
    private SimpleTaskList taskList;

    public TaskListGanttItemWrapper(SimpleTaskList taskList, Date minDate, Date maxDate) {
        super(minDate, maxDate);
        this.taskList = taskList;
        this.ownStep = generateStep();
    }

    @Override
    public String getName() {
        return taskList.getName();
    }

    @Override
    public List<GanttItemWrapper> subTasks() {
        List<SimpleTask> subTasks = projectTaskService.findSubTasksOfGroup(taskList.getId(), AppContext.getAccountId());
        if (subItems == null) {
            subItems = new ArrayList<>();
            for (SimpleTask subTask : subTasks) {
                TaskGanttItemWrapper subItem = new TaskGanttItemWrapper(subTask, minDate, maxDate);
                subItem.setParent(this);
                subItems.add(subItem);
            }
        }

        return subItems;
    }

    @Override
    Date getStartDate() {
        return taskList.getStartDate();
    }

    @Override
    Date getEndDate() {
        return taskList.getEndDate();
    }

    @Override
    String buildCaption() {
        return taskList.getName();
    }

    @Override
    String buildTooltip() {
        return ProjectTooltipGenerator.generateToolTipTaskList(AppContext.getUserLocale(), taskList, AppContext
                .getSiteUrl(), AppContext.getTimezone());
    }
}
