package com.mycollab.module.project.domain;


import com.mycollab.core.arguments.NotBindable;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class MilestoneGanttItem extends AssignWithPredecessors {
    @NotBindable
    private List<TaskGanttItem> subTasks;

    public List<TaskGanttItem> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<TaskGanttItem> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public boolean hasSubAssignments() {
        return CollectionUtils.isNotEmpty(subTasks);
    }

    public void removeSubTask(TaskGanttItem subTask) {
        subTasks.remove(subTask);
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
}
