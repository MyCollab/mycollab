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
package com.mycollab.reporting.expression

import net.sf.dynamicreports.report.definition.ReportParameters
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
class PrimaryTypeFieldExpression<T>(field: String) : SimpleFieldExpression<T>(field) {

    override fun evaluate(param: ReportParameters): T? = try {
        param.getFieldValue<T>(field)
    } catch (e: Exception) {
        LOG.error("Error while do report", e)
        null
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(PrimaryTypeFieldExpression::class.java)
        private val serialVersionUID = 1L
    }
}