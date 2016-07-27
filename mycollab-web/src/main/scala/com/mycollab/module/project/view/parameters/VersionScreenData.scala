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
package com.mycollab.module.project.view.parameters

import com.mycollab.module.tracker.domain.Version
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
object VersionScreenData {

  class Read(param: Integer) extends ScreenData[Integer](param) {}

  class Add(param: Version) extends ScreenData[Version](param) {}

  class Edit(param: Version) extends ScreenData[Version](param) {}

  class Search(param: VersionSearchCriteria) extends ScreenData[VersionSearchCriteria](param) {}

}
