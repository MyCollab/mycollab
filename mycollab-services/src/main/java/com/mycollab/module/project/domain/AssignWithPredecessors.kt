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
import java.time.LocalDateTime

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
abstract class AssignWithPredecessors {
    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null
    var deadline: LocalDateTime? = null
    var projectName: String? = null
    var name: String? = null
    var duration: Long? = null
    var ganttIndex: Int? = null
    var prjKey: String? = null
    var prjId: Int? = null
    var assignUser: String? = null
    var assignUserFullName: String? = null
    var assignUserAvatarId: String? = null
    var sAccountId: Int? = null
    var status: String? = null
    var type: String? = null
    open var progress: Double? = null
    var id: Int? = null

    @NotBindable
    var predecessors: List<TaskPredecessor>? = null

    @NotBindable
    var dependents: List<TaskPredecessor>? = null

    open fun hasSubAssignments(): Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AssignWithPredecessors) return false

        val that = other as AssignWithPredecessors?
        if (prjId != that!!.prjId) return false
        return if (type != that.type) false else !if (id != null) id != that.id else that.id != null

    }

    override fun hashCode(): Int {
        var result = 31 + prjId!!.hashCode()
        result = 31 * result + type!!.hashCode()
        result = 31 * result + if (id != null) id!!.hashCode() else 0
        return result
    }
}
