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

import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.ui.format._
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
@Component
class ProjectAuditLogRegistry extends InitializingBean {
  @Autowired private val auditLogRegistry: AuditLogRegistry = null
  
  override def afterPropertiesSet(): Unit = {
    auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.BUG, BugFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.TASK, TaskFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.MILESTONE, MilestoneFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.RISK, RiskFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.BUG_COMPONENT, ComponentFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.BUG_VERSION, VersionFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.INVOICE, InvoiceFieldFormatter.instance)
  }
}
