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
package com.mycollab.module.user.accountsettings.view.parameters

import com.mycollab.module.user.domain.Role
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
  * @author MyCollab Ltd.
  * @since 5.1.0
  */
object RoleScreenData {
  
  class Read(params: Integer) extends ScreenData[Integer](params) {}
  
  class Add(params: Role) extends ScreenData[Role](params) {}
  
  class Edit(params: Role) extends ScreenData[Role](params) {}
  
  class Search(params: RoleSearchCriteria) extends ScreenData[RoleSearchCriteria](params) {}
  
}
