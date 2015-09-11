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
package com.esofthead.mycollab.module.project.domain;


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
