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


import com.esofthead.mycollab.core.arguments.NotBindable;
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
