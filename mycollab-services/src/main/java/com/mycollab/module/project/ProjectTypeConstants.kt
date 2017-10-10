/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
object ProjectTypeConstants {
    @JvmField val PROJECT = "Project"

    @JvmField val PROJECT_ROLE = "project_role"

    @JvmField val TICKET = "Project-Assignment"

    @JvmField val TASK = "Project-Task"

    @JvmField val MESSAGE = "Project-Message"

    @JvmField val MILESTONE = "Project-Milestone"

    @JvmField val RISK = "Project-Risk"

    @JvmField val BUG = "Project-Bug"

    @JvmField val BUG_COMPONENT = "Project-Component"

    @JvmField val BUG_VERSION = "Project-Version"

    @JvmField val STANDUP = "Project-StandUp"

    @JvmField val PAGE = "Project-Page"

    @JvmField val DASHBOARD = "Project-Dashboard"

    @JvmField val FILE = "Project-File"

    @JvmField val TIME = "Project-Time"

    @JvmField val INVOICE = "Project-Invoice"

    @JvmField val FINANCE = "Project-Finance"

    @JvmField val REPORTS = "Project-Reports"

    @JvmField val MEMBER = "Project-Member"
}
