/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.domain;

import com.mycollab.core.arguments.NotBindable;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class TaskGanttItem extends AssignWithPredecessors {
    @NotBindable
    private List<TaskGanttItem> subTasks;

    private Integer parentTaskId;

    private Integer milestoneId;

    public List<TaskGanttItem> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<TaskGanttItem> subTasks) {
        this.subTasks = subTasks;
    }

    public void removeSubTask(TaskGanttItem subTask) {
        subTasks.remove(subTask);
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Integer parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Integer getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Integer milestoneId) {
        this.milestoneId = milestoneId;
    }

    @Override
    public boolean hasSubAssignments() {
        return CollectionUtils.isNotEmpty(subTasks);
    }

    @Override
    public Double getProgress() {
        if (hasSubAssignments()) {
            Double summary = 0d;
            for (TaskGanttItem item : subTasks) {
                if (item.getProgress() != null) {
                    summary += item.getProgress();
                }
            }
            return summary / subTasks.size();
        } else {
            return super.getProgress();
        }
    }

    public Task buildNewTask() {
        Task newTask = new Task();
        newTask.setName(getName());
        newTask.setStartdate(getStartDate());
        newTask.setEnddate(getEndDate());
        newTask.setPercentagecomplete(getProgress());
        newTask.setGanttindex(getGanttIndex());
        newTask.setParenttaskid(getParentTaskId());
        newTask.setMilestoneid(getMilestoneId());
        newTask.setProjectid(getPrjId());
        newTask.setSaccountid(getsAccountId());
        return newTask;
    }
}
