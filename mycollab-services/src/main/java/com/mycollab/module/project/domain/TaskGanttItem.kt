/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.domain

import com.mycollab.core.arguments.NotBindable
import org.apache.commons.collections.CollectionUtils

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
class TaskGanttItem : AssignWithPredecessors() {
    @NotBindable
    var subTasks: MutableList<TaskGanttItem>? = null

    var parentTaskId: Int? = null

    var milestoneId: Int? = null

    fun removeSubTask(subTask: TaskGanttItem) {
        subTasks?.remove(subTask)
    }

    override fun hasSubAssignments(): Boolean = CollectionUtils.isNotEmpty(subTasks)

    override var progress: Double? = null
        get() =
            when {
                hasSubAssignments() -> {
                    val summary = subTasks!!
                            .filter { it.progress != null }
                            .sumByDouble { it.progress ?: 0.0 }
                    summary / subTasks!!.size
                }
                else -> super.progress
            }


    fun buildNewTask(): Task {
        val newTask = Task()
        newTask.name = name
        newTask.startdate = startDate
        newTask.enddate = endDate
        newTask.percentagecomplete = progress
        newTask.ganttindex = ganttIndex
        newTask.parenttaskid = parentTaskId
        newTask.milestoneid = milestoneId
        newTask.projectid = prjId
        newTask.saccountid = sAccountId
        return newTask
    }
}
