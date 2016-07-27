/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.arguments

import scala.beans.BeanProperty

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
@SerialVersionUID(1L)
class SearchRequest(@BeanProperty var currentPage: Integer, @BeanProperty var numberOfItems: Integer) {
  @BeanProperty val requestedUser: String = GroupIdProvider.getRequestedUser
}

object SearchRequest {
  val DEFAULT_NUMBER_SEARCH_ITEMS = 25
}
