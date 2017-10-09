package com.mycollab.module.project.domain;


import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class ProjectGanttItem extends AssignWithPredecessors {
    private List<MilestoneGanttItem> milestones;

    private List<TaskGanttItem> tasksWithNoMilestones;

    public List<MilestoneGanttItem> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneGanttItem> milestones) {
        this.milestones = milestones;
    }

    public List<TaskGanttItem> getTasksWithNoMilestones() {
        return tasksWithNoMilestones;
    }

    public void setTasksWithNoMilestones(List<TaskGanttItem> tasksWithNoMilestones) {
        this.tasksWithNoMilestones = tasksWithNoMilestones;
    }

    @Override
    public boolean hasSubAssignments() {
        return CollectionUtils.isNotEmpty(milestones) || CollectionUtils.isNotEmpty(tasksWithNoMilestones);
    }
}
