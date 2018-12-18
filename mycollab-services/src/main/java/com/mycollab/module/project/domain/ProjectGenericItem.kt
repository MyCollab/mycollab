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

import com.mycollab.module.project.ProjectTypeConstants
import java.time.LocalDateTime

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
class ProjectGenericItem {
    lateinit var type: String

    lateinit var typeId: String

    var createdUser: String? = null

    var createdUserAvatarId: String? = null

    var createdUserDisplayName: String? = null

    var createdTime: LocalDateTime? = null

    var lastUpdatedTime: LocalDateTime? = null

    var name: String? = null

    var description: String? = null

    var projectId: Int? = null

    lateinit var projectName: String

    lateinit var projectShortName: String

    var extraTypeId: Int? = null

    val isBug: Boolean
        get() = ProjectTypeConstants.BUG == type

    val isTask: Boolean
        get() = ProjectTypeConstants.TASK == type
}
