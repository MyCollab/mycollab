/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.html

import com.hp.gagawa.java.elements.{Div, Text}

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class DivLessFormatter extends Div {

    override def write: String = {
        val b: StringBuffer = new StringBuffer
        if ((this.children != null) && (this.children.size > 0)) {
            import scala.collection.JavaConversions._
            for (child <- this.children) {
                b.append(child.write)
            }
        }
        return b.toString
    }
}

object DivLessFormatter {
    val EMPTY_SPACE = new Text("&nbsp;")
}
