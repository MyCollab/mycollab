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
package com.mycollab.common.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.mycollab.core.arguments.ValuedBean
import org.apache.commons.lang.StringUtils

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class MailRecipientField @JsonCreator
constructor(var email: String, name: String?) : ValuedBean() {
    var name = if (StringUtils.isNotBlank(name)) name else email
}
