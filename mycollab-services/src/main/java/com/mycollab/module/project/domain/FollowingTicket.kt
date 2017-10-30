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
package com.mycollab.module.project.domain

import java.io.Serializable
import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class FollowingTicket : Serializable {

    var projectId: Int? = null

    lateinit var projectName: String

    var type: String? = null

    var typeId: Int? = null

    var name: String? = null

    var status: String? = null

    var dueDate: Date? = null

    var assignUser: String? = null

    var assignUserAvatarId: String? = null

    var assignUserFullName: String? = null

    var monitorDate: Date? = null
}
