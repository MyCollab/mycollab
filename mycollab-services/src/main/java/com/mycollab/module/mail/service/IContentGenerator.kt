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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.mail.service

import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
interface IContentGenerator {
    /**
     *
     * @param key
     * @param value
     */
    fun putVariable(key: String, value: Any?)

    /**
     *
     * @param templateFilePath
     * @return
     */
    fun parseFile(templateFilePath: String): String

    /**
     *
     * @param templateFilePath
     * @param currentLocale
     * @return
     */
    fun parseFile(templateFilePath: String, currentLocale: Locale?): String
}