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
package com.mycollab.module.project.view.service

import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.vaadin.mvp.CacheableComponent
import com.vaadin.ui.AbstractComponent

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
interface BugComponentFactory {
    fun createPriorityPopupField(bug: SimpleBug): AbstractComponent

    fun createAssigneePopupField(bug: SimpleBug): AbstractComponent

    fun createCommentsPopupField(bug: SimpleBug): AbstractComponent

    fun createStatusPopupField(bug: SimpleBug): AbstractComponent

    fun createMilestonePopupField(bug: SimpleBug): AbstractComponent

    fun createDeadlinePopupField(bug: SimpleBug): AbstractComponent

    fun createStartDatePopupField(bug: SimpleBug): AbstractComponent

    fun createEndDatePopupField(bug: SimpleBug): AbstractComponent

    fun createBillableHoursPopupField(bug: SimpleBug): AbstractComponent

    fun createNonbillableHoursPopupField(bug: SimpleBug): AbstractComponent

    fun createFollowersPopupField(bug: SimpleBug): AbstractComponent
}
