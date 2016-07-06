/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.vaadin.mvp.ScreenData
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object ProjectScreenData {

  class Goto(param: Integer) extends ScreenData[Integer](param) {}

  class Add(param: SimpleProject) extends ScreenData[SimpleProject](param) {}

  class Edit(param: Integer) extends ScreenData[Integer](param) {}

  class ProjectActivities(param: ActivityStreamSearchCriteria) extends ScreenData[ActivityStreamSearchCriteria](param) {}

  class GotoDashboard() extends ScreenData[AnyRef](null) {}

  class AllActivities(param: ActivityStreamSearchCriteria) extends ScreenData[ActivityStreamSearchCriteria](param) {}

}
