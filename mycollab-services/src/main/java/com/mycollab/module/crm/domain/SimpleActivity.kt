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
package com.mycollab.module.crm.domain

import com.mycollab.core.arguments.ValuedBean
import com.mycollab.core.utils.DateTimeUtils

import java.io.Serializable
import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleActivity : ValuedBean(), Serializable {

    var id: Int? = null

    var status: String? = null

    var eventType: String? = null

    var subject: String? = null

    var type: String? = null

    var typeId: Int? = null

    var typeName: String? = null

    var startDate: Date? = null

    var endDate: Date? = null

    var assignUser: String? = null

    var assignUserFullName: String? = null

    var createdTime: Date? = null

    var lastUpdatedTime: Date? = null

    var description: String? = null

    // --- get for CrmTask
    var relatedTo: String? = null
    var priority: String? = null
    var contact: String? = null
    var contactId: Int? = null

    // --- get for Call
    var callDuration: Int? = null
    var callPurpose: String? = null
    var callResult: String? = null
    // --- get for Meeting
    var meetingLocation: String? = null
    var assignUserAvatarId: String? = null

    val isCompleted: Boolean
        get() = "Held" == status

    val isOverdue: Boolean
        get() = (!isCompleted && endDate != null
                && endDate!!
                .before(DateTimeUtils.getCurrentDateWithoutMS()))

    fun getContactFullName(): String? {
        return contact
    }

    fun setContactFullName(contactFullName: String) {
        this.contact = contactFullName
    }
}
