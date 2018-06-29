/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.domain

import com.mycollab.core.arguments.NotBindable
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

import java.util.Calendar

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
open class Resource : Comparable<Resource> {

    @NotBindable
    var isSelected = false

    var uuid = ""
    var createdBy = ""
    var created: Calendar? = null
    var path = ""
    var description: String? = null

    // length is Kilobyte value
    var size: Long = 0L
    var createdUser: String? = null
    var name: String? = null
        get() {
            return if (field == null) {
                val index = path.lastIndexOf("/")
                path.substring(index + 1)
            } else field
        }

    val isExternalResource: Boolean
        get() = this is ExternalFolder || this is ExternalContent

    override fun compareTo(other: Resource): Int {
        return if (this is Folder && other is Content) {
            -1
        } else if (this is Folder && other is Folder) {
            if (this.created != null && other.created != null) {
                this.created!!.time.compareTo(other.created!!.time)
            } else {
                this.name!!.compareTo(other.name!!)
            }
        } else {
            1
        }
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(1, 101).append(path).build()!!
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other === this) {
            return true
        }
        if (other.javaClass != javaClass) {
            return false
        }
        val res = other as Resource?
        return EqualsBuilder().append(path, res!!.path).build()!!
    }
}
