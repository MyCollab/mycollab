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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object ProjectRolePermissionCollections {

    const val MESSAGES = "Message"

    const val MILESTONES = "Milestone"

    const val INVOICE = "Invoice"

    const val TIME = "Time"

    const val FINANCE = "Finance"

    const val TASKS = "Task"

    const val BUGS = "Bug"

    const val VERSIONS = "Version"

    const val COMPONENTS = "Component"

    const val RISKS = "Risk"

    const val SPIKE = "Spike"

    const val USERS = "User"

    const val ROLES = "Role"

    const val FILES = "File"

    const val PAGES = "Page"

    const val PROJECT = "Project"

    const val APPROVE_TIMESHEET = "Approve_Timesheet"

    @JvmField
    val PROJECT_PERMISSIONS = arrayOf(MESSAGES, MILESTONES, TASKS, BUGS, COMPONENTS, VERSIONS, FILES, PAGES, RISKS, TIME, INVOICE,
            USERS, ROLES, PROJECT, FINANCE, APPROVE_TIMESHEET)
}
