/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui.registry

import com.mycollab.common.domain.{AuditChangeItem, SimpleActivityStream}
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
@Component
class AuditLogRegistry extends InitializingBean {
  private var auditPrinters: Map[String, FieldGroupFormatter] = Map[String, FieldGroupFormatter]()
  
  override def afterPropertiesSet(): Unit = {
    
  }
  
  def registerAuditLogHandler(typeVal: String, fieldGroupFormatter: FieldGroupFormatter) {
    auditPrinters += (typeVal -> fieldGroupFormatter)
  }
  
  def generatorDetailChangeOfActivity(activityStream: SimpleActivityStream): String = {
    if (activityStream.getAssoAuditLog != null) {
      val groupFormatter = auditPrinters(activityStream.getType)
      if (groupFormatter != null) {
        val str = new StringBuilder("")
        var isAppended = false
        val changeItems: java.util.List[AuditChangeItem] = activityStream.getAssoAuditLog.getChangeItems
        if (CollectionUtils.isNotEmpty(changeItems)) {
          import scala.collection.JavaConversions._
          for (item <- changeItems) {
            val fieldName = item.getField
            val fieldDisplayHandler = groupFormatter.getFieldDisplayHandler(fieldName)
            if (fieldDisplayHandler != null) {
              isAppended = true
              str.append(fieldDisplayHandler.generateLogItem(item))
            }
          }
        }
        if (isAppended) {
          str.insert(0, "<p>").insert(0, "<ul>")
          str.append("</ul>").append("</p>")
        }
        return str.toString
      }
    }
    ""
  }
  
  def getFieldGroupFormatter(typeVal: String): FieldGroupFormatter = {
    auditPrinters(typeVal)
  }
}

object AuditLogRegistry {
  def getFieldGroupFormatterOfType(typeVal: String): FieldGroupFormatter = {
    val auditLogRegistry = AppContextUtil.getSpringBean(classOf[AuditLogRegistry])
    auditLogRegistry.getFieldGroupFormatter(typeVal)
  }
}
