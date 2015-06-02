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

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.8
 */
public class TaskGanttItemWrapper extends GanttItemWrapper {
    private ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
    private SimpleTask task;
    private Date startDate, endDate;

    public TaskGanttItemWrapper(SimpleTask task, Date minDate, Date maxDate) {
        super(minDate, maxDate);
        this.task = task;
        calculateDates();
        this.ownStep = generateStep();
    }

    public SimpleTask getTask() {
        return task;
    }

    @Override
    public String getName() {
        return task.getTaskname();
    }

    @Override
    public List<GanttItemWrapper> subTasks() {
        List<SimpleTask> subTasks = projectTaskService.findSubTasks(task.getId(), AppContext.getAccountId());
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

    private void calculateDates() {
        startDate = task.getStartdate();
        endDate = task.getEnddate();

        if (endDate == null) {
            endDate = task.getDeadline();
        }

        if (startDate == null) {
            if (endDate == null) {
                startDate = DateTimeUtils.getCurrentDateWithoutMS();
                endDate = DateTimeUtils.subtractOrAddDayDuration(startDate, 1);
            } else {
                endDate = DateTimeUtils.trimHMSOfDate(endDate);
                startDate = DateTimeUtils.subtractOrAddDayDuration(endDate, -1);
            }
        } else {
            startDate = DateTimeUtils.trimHMSOfDate(startDate);
            if (endDate == null) {
                endDate = DateTimeUtils.subtractOrAddDayDuration(startDate, 1);
            } else {
                endDate = DateTimeUtils.trimHMSOfDate(endDate);
                endDate = DateTimeUtils.subtractOrAddDayDuration(endDate, 1);
            }
        }
    }

    @Override
    Date getStartDate() {
        return startDate;
    }

    @Override
    Date getEndDate() {
        return endDate;
    }

    @Override
    String buildCaption() {
        return task.getTaskname();
    }

    @Override
    String buildTooltip() {
        return ProjectTooltipGenerator.generateToolTipTask(AppContext.getUserLocale(), task, AppContext.getSiteUrl(),
                AppContext.getTimezone());
    }
}
