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
package com.mycollab.vaadin.ui.registry

import com.mycollab.common.domain.SimpleActivityStream
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class AuditLogRegistry : InitializingBean {
    companion object {
        @JvmStatic fun getFieldGroupFormatterOfType(typeVal: String): FieldGroupFormatter {
            val auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry::class.java)
            return auditLogRegistry.getFieldGroupFormatter(typeVal)
        }
    }

    private var auditPrinters = mutableMapOf<String, FieldGroupFormatter>()

    override fun afterPropertiesSet() {}

    fun registerAuditLogHandler(typeVal: String, fieldGroupFormatter: FieldGroupFormatter) {
        auditPrinters.put(typeVal, fieldGroupFormatter)
    }

    fun generatorDetailChangeOfActivity(activityStream: SimpleActivityStream): String {
        if (activityStream.assoAuditLog != null) {
            val value = auditPrinters[activityStream.type]
            when {
                value != null -> {
                    val str = StringBuilder("")
                    var isAppended = false
                    val changeItems = activityStream.assoAuditLog.changeItems
                    if (CollectionUtils.isNotEmpty(changeItems)) {
                        changeItems.forEach {
                            val fieldName = it.field
                            val fieldDisplayHandler = value.getFieldDisplayHandler(fieldName)
                            if (fieldDisplayHandler != null) {
                                isAppended = true
                                str.append(fieldDisplayHandler.generateLogItem(it))
                            }
                        }
                    }
                    if (isAppended) {
                        str.insert(0, "<p>").insert(0, "<ul>")
                        str.append("</ul>").append("</p>")
                    }
                    return str.toString()
                }
                else -> return ""
            }
        } else return ""
    }

    fun getFieldGroupFormatter(typeVal: String): FieldGroupFormatter =
            auditPrinters[typeVal] ?: FieldGroupFormatter()
}