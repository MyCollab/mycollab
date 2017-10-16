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
package com.mycollab.aspect

import com.mycollab.common.domain.AuditChangeItem
import com.mycollab.core.utils.JsonDeSerializer
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory
import java.beans.Introspector
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
object AuditLogUtil {
    private val LOG = LoggerFactory.getLogger(AuditLogUtil::class.java)

    @JvmStatic
    fun getChangeSet(oldObj: Any, newObj: Any, excludeFields: List<String>, isSelective: Boolean): String? {
        val cl = oldObj.javaClass
        val changeItems = ArrayList<AuditChangeItem>()

        try {
            val beanInfo = Introspector.getBeanInfo(cl, Any::class.java)

            for (propertyDescriptor in beanInfo.propertyDescriptors) {
                val fieldName = propertyDescriptor.name
                if (excludeFields.contains(fieldName)) {
                    continue
                }
                val oldProp = getValue(PropertyUtils.getProperty(oldObj, fieldName))

                val newPropVal: Any
                try {
                    newPropVal = PropertyUtils.getProperty(newObj, fieldName)
                } catch (e: Exception) {
                    continue
                }

                val newProp = getValue(newPropVal)

                if (oldProp != newProp) {
                    if (isSelective && newProp == "") {
                    } else {
                        val changeItem = AuditChangeItem()
                        changeItem.field = fieldName
                        changeItem.newvalue = newProp
                        changeItem.oldvalue = oldProp
                        changeItems.add(changeItem)
                    }
                }
            }
        } catch (e: Exception) {
            LOG.error("There is error when convert changeset", e)
            return null
        }

        return if (changeItems.size > 0) JsonDeSerializer.toJson(changeItems) else null
    }

    private fun getValue(obj: Any?): String {
        return when {
            obj != null -> when (obj) {
                is Date -> formatDateW3C(obj)
                else -> obj.toString()
            }
            else -> ""
        }
    }

    private fun formatDateW3C(date: Date): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        val text = df.format(date)
        return "${text.substring(0, 22)}:${text.substring(22)}"
    }
}
