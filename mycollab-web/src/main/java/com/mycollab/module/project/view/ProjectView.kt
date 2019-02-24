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
package com.mycollab.module.project.view

import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.vaadin.mvp.PageView
import com.vaadin.ui.Component

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectView : PageView {

    fun initView(project: SimpleProject)

    fun updateProjectFeatures()

    fun gotoSubView(name: String, viewDisplay: Component): Component

    fun setNavigatorVisibility(visibility: Boolean)

    fun addComponentToRightBar(component: Component)

    fun clearRightBar()

    companion object {
        const val SUMMARY_ENTRY = "Summary"

        const val MESSAGE_ENTRY = "Message"

        const val MILESTONE_ENTRY = "Milestone"

        const val TICKET_ENTRY = "Ticket"

        const val KANBAN_ENTRY = "Kanban"

        const val FILE_ENTRY = "File"

        const val PAGE_ENTRY = "Page"

        const val FINANCE_ENTRY = "Financing"

        const val TIME_TRACKING_ENTRY = "Time-Tracking"

        const val INVOICE_ENTRY = "Invoice"

        const val SETTING = "Setting"

        const val USERS_ENTRY = "Users"

        const val ROLE_ENTRY = "Roles"

        const val COMPONENT_ENTRY = "Component";

        const val VERSION_ENTRY = "Version";

        const val CUSTOM_ENTRY = "Custom"
    }
}
