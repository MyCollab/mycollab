package com.mycollab.common.service

import com.mycollab.common.domain.AuditLog
import com.mycollab.common.domain.SimpleAuditLog
import com.mycollab.common.domain.criteria.AuditLogSearchCriteria
import com.mycollab.db.persistence.service.IDefaultService

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface AuditLogService : IDefaultService<Int, AuditLog, AuditLogSearchCriteria> {

    fun findLastestLog(auditLogId: Int, sAccountId: Int): SimpleAuditLog
}
