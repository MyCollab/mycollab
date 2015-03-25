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

import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs
import com.esofthead.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
object BugScreenData {

  class GotoDashboard extends ScreenData {}

  class Search(params: BugFilterParameter) extends ScreenData[BugFilterParameter](params) {}

  class Read(params: Integer) extends ScreenData[Integer](params) {}

  class Add(params: BugWithBLOBs) extends ScreenData[BugWithBLOBs](params) {}

  class Edit(params: BugWithBLOBs) extends ScreenData[BugWithBLOBs](params) {}
}
