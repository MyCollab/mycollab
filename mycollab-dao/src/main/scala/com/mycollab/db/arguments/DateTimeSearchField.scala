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

import java.util.Date

import com.mycollab.core.utils.DateTimeUtils

import scala.beans.BeanProperty

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
class DateTimeSearchField(operation: String, @BeanProperty val comparison: String, dateVal: Date) extends SearchField(operation) {
  @BeanProperty val value = DateTimeUtils.convertDateTimeToUTC(dateVal)
}

object DateTimeSearchField {
  val LESS_THAN = "<"
  val LESS_THAN_EQUAL = "<="
  val GREATER_THAN = ">"
  val GREATER_THAN_EQUAL = ">="
  val EQUAL = "="
  val NOT_EQUAL = "<>"
}
