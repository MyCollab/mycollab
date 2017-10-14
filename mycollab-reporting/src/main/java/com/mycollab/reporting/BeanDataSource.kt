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
package com.mycollab.reporting

import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JRField
import org.apache.commons.beanutils.PropertyUtils

import java.util.ArrayList

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BeanDataSource<T>(val data: List<T>) : JRDataSource {
    private var currentIndex = 0
    private var currentRecord: T? = null

    @Throws(JRException::class)
    override fun getFieldValue(jrField: JRField): Any {
        try {
            val fieldName = jrField.name
            return PropertyUtils.getProperty(currentRecord, fieldName)
        } catch (e: Exception) {
            throw JRException(e)
        }

    }

    @Throws(JRException::class)
    override fun next(): Boolean {
        val result = currentIndex < data.size
        if (result) {
            currentRecord = data[currentIndex]
            currentIndex++
        }

        return result
    }

}
