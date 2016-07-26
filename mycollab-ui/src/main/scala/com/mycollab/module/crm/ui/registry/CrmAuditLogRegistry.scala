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
package com.mycollab.module.crm.ui.registry

import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.ui.format._
import com.mycollab.vaadin.ui.registry.AuditLogRegistry
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
@Component
class CrmAuditLogRegistry extends InitializingBean {
  @Autowired private val auditLogRegistry: AuditLogRegistry = null
  
  override def afterPropertiesSet(): Unit = {
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.ACCOUNT, AccountFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.CONTACT, ContactFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.CAMPAIGN, CampaignFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.LEAD, LeadFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.OPPORTUNITY, OpportunityFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.CASE, CaseFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.MEETING, MeetingFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.TASK, AssignmentFieldFormatter.instance)
    auditLogRegistry.registerAuditLogHandler(CrmTypeConstants.CALL, CallFieldFormatter.instance)
  }
}
