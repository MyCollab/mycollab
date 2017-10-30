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
package com.mycollab.module.page.domain

import com.mycollab.core.arguments.NotBindable

import javax.validation.constraints.NotNull
import java.util.Calendar

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
class Page : PageResource() {

    @NotNull(message = "Subject must be not null")
    lateinit var subject: String

    @NotNull(message = "Content must be not null")
    lateinit var content: String

    @NotBindable
    var isLock = false

    @NotBindable
    var isNew = true

    var category: String? = null

    var status: String? = null

    var lastUpdatedUser: String? = null

    @NotBindable
    var lastUpdatedTime: Calendar? = null
}
