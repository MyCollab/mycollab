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

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import org.tltv.gantt.client.shared.Step;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.8
 */
public class GanttItemWrapper {
    private ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
    private SimpleTask task;
    private Date startDate, endDate;
    private GanttItemWrapper parent;
    private Step ownStep;
    private List<GanttItemWrapper> subItems;

    public GanttItemWrapper(SimpleTask task) {
        this.task = task;
        calculateDates();
        this.ownStep = generateStep();
    }

    public SimpleTask getTask() {
        return task;
    }

    public void setTask(SimpleTask task) {
        this.task = task;
    }

    public boolean hasSubTasks() {
        return (task.getNumSubTasks() != null && task.getNumSubTasks() > 0);
    }

    public String getName() {
        return task.getTaskname();
    }

    public List<GanttItemWrapper> subTasks(SearchCriteria.OrderField orderField) {
        List<SimpleTask> subTasks = projectTaskService.findSubTasks(task.getId(), AppContext.getAccountId(), orderField);
        if (subItems == null) {
            subItems = new ArrayList<>();
            for (SimpleTask subTask : subTasks) {
                GanttItemWrapper subItem = new GanttItemWrapper(subTask);
                subItem.setParent(this);
                subItems.add(subItem);
            }
        }

        return subItems;
    }

    public Integer getId() {
        return task.getId();
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

    private static final long DAY_IN_MILIS = 1000*60*60*24;

    public Double getDuration() {
        if (startDate != null && endDate!= null) {
            return (endDate.getTime() - startDate.getTime())* 1d /DAY_IN_MILIS;
        } else {
            return 0d;
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getActualStartDate() {
        return task.getActualstartdate();
    }

    public Date getActualEndDate() {
        return task.getActualenddate();
    }

    public Double getPercentageComplete() {
        return task.getPercentagecomplete();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        task.setStartdate(startDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        task.setEnddate(endDate);
    }

    public String getAssignUser() {
        return task.getAssignuser();
    }

    String buildCaption() {
        return task.getTaskname();
    }

    String buildTooltip() {
        return ProjectTooltipGenerator.generateToolTipTask(AppContext.getUserLocale(), task, AppContext.getSiteUrl(),
                AppContext.getTimezone());
    }

    public void markAsDirty() {
        calculateDates();
        ownStep.setCaption(buildCaption());
        ownStep.setDescription(buildTooltip());
        ownStep.setStartDate(startDate);
        ownStep.setEndDate(endDate);
    }

    StepExt generateStep() {
        StepExt step = new StepExt();
        step.setCaption(buildCaption());
        step.setCaptionMode(Step.CaptionMode.HTML);
        step.setDescription(buildTooltip());
        step.setStartDate(startDate);
        step.setEndDate(endDate);
        step.setGanttItemWrapper(this);
        step.setProgress(task.getPercentagecomplete());
//        step.setShowProgress(true);
        return step;
    }

    public GanttItemWrapper getParent() {
        return parent;
    }

    public void setParent(GanttItemWrapper parent) {
        this.parent = parent;
    }

    public Step getStep() {
        return ownStep;
    }
}
