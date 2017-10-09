package com.mycollab.vaadin.ui.registry

import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.ui.format.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class ProjectAuditLogRegistry(private val auditLogRegistry: AuditLogRegistry) : InitializingBean {

    override fun afterPropertiesSet() {
        auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.BUG, BugFieldFormatter.instance())
        auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.TASK, TaskFieldFormatter.instance())
        auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.MILESTONE, MilestoneFieldFormatter.instance())
        auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.RISK, RiskFieldFormatter.instance())
        auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.BUG_COMPONENT, ComponentFieldFormatter.instance())
        auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.BUG_VERSION, VersionFieldFormatter.instance())
        auditLogRegistry.registerAuditLogHandler(ProjectTypeConstants.INVOICE, InvoiceFieldFormatter.instance())
    }
}