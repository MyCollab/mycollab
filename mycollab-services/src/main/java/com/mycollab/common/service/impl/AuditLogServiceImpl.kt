package com.mycollab.common.service.impl

import com.mycollab.common.dao.AuditLogMapper
import com.mycollab.common.dao.AuditLogMapperExt
import com.mycollab.common.domain.AuditLog
import com.mycollab.common.domain.SimpleAuditLog
import com.mycollab.common.domain.criteria.AuditLogSearchCriteria
import com.mycollab.common.service.AuditLogService
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class AuditLogServiceImpl(private val auditLogMapper: AuditLogMapper,
                          private val auditLogMapperExt: AuditLogMapperExt) : DefaultService<Int, AuditLog, AuditLogSearchCriteria>(), AuditLogService {

    override val crudMapper: ICrudGenericDAO<Int, AuditLog>
        get() = auditLogMapper as ICrudGenericDAO<Int, AuditLog>

    override val searchMapper: ISearchableDAO<AuditLogSearchCriteria>
        get() = auditLogMapperExt

    override fun findLastestLogs(auditLogId: Int, sAccountId: Int): SimpleAuditLog? =
            auditLogMapperExt.findLatestLog(auditLogId)
}
