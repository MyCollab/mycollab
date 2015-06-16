/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email

import com.esofthead.mycollab.schedule.email.format.{DefaultFieldFormat, FieldFormat}

import scala.collection.mutable._

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class ItemFieldMapper {
    private val fieldNameMap: Map[String, FieldFormat] = LinkedHashMap()

    def put(fieldName: Enum[_], displayName: Enum[_]): Unit = {
        fieldNameMap.put(fieldName.name(), new DefaultFieldFormat(fieldName.name, displayName))
    }

    def put(fieldName: Enum[_], displayName: Enum[_], isColSpan: Boolean): Unit = {
        fieldNameMap.put(fieldName.name(), new DefaultFieldFormat(fieldName.name, displayName, isColSpan))
    }

    def put(fieldName: Enum[_], format: FieldFormat): Unit = {
        fieldNameMap.put(fieldName.name, format)
    }

    def keySet(): java.util.Set[String] = {
        import scala.collection.JavaConverters._
        fieldNameMap.keySet.asJava
    }

    def hasField(fieldName: String): Boolean = fieldNameMap.contains(fieldName)

    def getFieldLabel(fieldName: String): FieldFormat = fieldNameMap(fieldName)
}
