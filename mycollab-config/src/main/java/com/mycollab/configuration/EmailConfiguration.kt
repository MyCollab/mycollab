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
package com.mycollab.configuration

import com.mycollab.core.MyCollabException
import com.mycollab.core.arguments.ValuedBean
import com.mycollab.core.utils.StringUtils

import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

/**
 * Email configuration of MyCollab
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
class EmailConfiguration internal constructor(@field:NotNull var host: String?,
                                              @field:NotNull var user: String?,
                                              @field:NotNull var password: String?,
                                              port: Int, isStartTls: Boolean, isSsl: Boolean, notifyEmail: String) : ValuedBean() {

    @Digits(integer = 6, fraction = 0)
    var port: Int = 25

    var isStartTls = false
    var isSsl = false
    var notifyEmail: String? = null

    init {
        this.port = port
        this.isStartTls = isStartTls
        this.isSsl = isSsl
        this.notifyEmail = if (StringUtils.isBlank(notifyEmail)) user else notifyEmail
    }
}
