package com.mycollab.common.dao;

import com.mycollab.common.domain.SimpleAuditLog;
import com.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.mycollab.db.persistence.ISearchableDAO;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public interface AuditLogMapperExt extends ISearchableDAO<AuditLogSearchCriteria> {
    SimpleAuditLog findLatestLog(int auditLogId);
}
