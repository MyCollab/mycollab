/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.module.project.view.service

import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.vaadin.mvp.CacheableComponent
import com.vaadin.ui.AbstractComponent

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
interface TaskComponentFactory {

    fun createAssigneePopupField(task: SimpleTask): AbstractComponent

    fun createPriorityPopupField(task: SimpleTask): AbstractComponent

    fun createCommentsPopupField(task: SimpleTask): AbstractComponent

    fun createStatusPopupField(task: SimpleTask): AbstractComponent

    fun createMilestonePopupField(task: SimpleTask): AbstractComponent

    fun createPercentagePopupField(task: SimpleTask): AbstractComponent

    fun createDeadlinePopupField(task: SimpleTask): AbstractComponent

    fun createStartDatePopupField(task: SimpleTask): AbstractComponent

    fun createEndDatePopupField(task: SimpleTask): AbstractComponent

    fun createBillableHoursPopupField(task: SimpleTask): AbstractComponent

    fun createNonBillableHoursPopupField(task: SimpleTask): AbstractComponent

    fun createFollowersPopupField(task: SimpleTask): AbstractComponent
}
