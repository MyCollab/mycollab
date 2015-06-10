/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common

import scala.beans.BeanProperty

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class TableViewField(@BeanProperty var descKey: Enum[_], @BeanProperty var field:String,
                     @BeanProperty var defaultWidth:Integer) extends Serializable {

  def this(descKey: Enum[_], field:String) = this(descKey, field, -1)
}
