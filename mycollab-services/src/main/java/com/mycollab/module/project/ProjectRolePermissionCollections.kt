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

    @JvmField
    val MESSAGES = "Message"

    @JvmField
    val MILESTONES = "Milestone"

    @JvmField
    val INVOICE = "Invoice"

    @JvmField
    val TIME = "Time"

    @JvmField
    val FINANCE = "Finance"

    @JvmField
    val TASKS = "Task"

    @JvmField
    val BUGS = "Bug"

    @JvmField
    val VERSIONS = "Version"

    @JvmField
    val COMPONENTS = "Component"

    @JvmField
    val RISKS = "Risk"

    @JvmField
    val SPIKE = "Spike"

    @JvmField
    val USERS = "User"

    @JvmField
    val ROLES = "Role"

    @JvmField
    val PAGES = "Page"

    @JvmField
    val PROJECT = "Project"

    @JvmField
    val APPROVE_TIMESHEET = "Approve_Timesheet"

    @JvmField
    val PROJECT_PERMISSIONS = arrayOf(MESSAGES, MILESTONES, TASKS, BUGS, COMPONENTS, VERSIONS, PAGES, RISKS, TIME, INVOICE,
            USERS, ROLES, PROJECT, FINANCE, APPROVE_TIMESHEET)
}
