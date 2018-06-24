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

import com.mycollab.core.arguments.ValuedBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Email configuration of MyCollab
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Component
@Profile("program")
@ConfigurationProperties(prefix = "mail")
open class EmailConfiguration(var smtphost: String?, var username: String?,
                              var password: String?, var port: Int,
                              var startTls: Boolean = false, var ssl: Boolean = false,
                              var notifyEmail: String) : ValuedBean() {
    constructor(): this("", "", "", -1, false, false, "")
}


