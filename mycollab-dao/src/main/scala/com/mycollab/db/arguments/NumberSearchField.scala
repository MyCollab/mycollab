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
class NumberSearchField(operation: String, @BeanProperty val value: Number,
                        @BeanProperty val compareOperator: String) extends SearchField(operation) {
  def this(value: Number) = this(SearchField.AND, value, NumberSearchField.EQUAL)
  
  def this(value: Number, compareOperator: String) = this(SearchField.AND, value, compareOperator)
}

object NumberSearchField {
  val EQUAL = "="
  val NOT_EQUAL = "<>"
  val LESS_THAN = "<"
  val GREATER = ">"
  
  def equal(value: Number): NumberSearchField = new NumberSearchField(SearchField.AND, value, EQUAL)
  
  def greaterThan(value: Number): NumberSearchField = new NumberSearchField(SearchField.AND, value, GREATER)
  
  def lessThan(value: Number): NumberSearchField = new NumberSearchField(SearchField.AND, value, LESS_THAN)
}
