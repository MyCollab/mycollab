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
