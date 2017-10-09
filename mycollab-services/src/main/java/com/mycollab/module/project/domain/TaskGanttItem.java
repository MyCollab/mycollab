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
