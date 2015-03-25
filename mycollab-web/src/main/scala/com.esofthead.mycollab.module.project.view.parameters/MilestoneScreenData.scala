/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.parameters

import com.esofthead.mycollab.module.project.domain.Milestone
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import com.esofthead.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
object MilestoneScreenData {

  class Read(params:Integer) extends ScreenData[Integer](params) {}

  class Edit(params:Milestone) extends ScreenData[Milestone](params) {}

  class Add(params:Milestone) extends ScreenData[Milestone](params) {}

  class Search(params:MilestoneSearchCriteria) extends ScreenData[MilestoneSearchCriteria](params) {}
}
