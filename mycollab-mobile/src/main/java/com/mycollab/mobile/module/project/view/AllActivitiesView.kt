/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.mobile.module.project.view

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.mobile.ui.IListView
import com.mycollab.module.project.domain.ProjectActivityStream

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
interface AllActivitiesView : IListView<ActivityStreamSearchCriteria, ProjectActivityStream>
