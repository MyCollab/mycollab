package com.mycollab.common.service.impl;

import com.mycollab.common.dao.AuditLogMapper;
import com.mycollab.common.dao.AuditLogMapperExt;
import com.mycollab.common.domain.AuditLog;
import com.mycollab.common.domain.SimpleAuditLog;
import com.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.mycollab.common.service.AuditLogService;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class AuditLogServiceImpl extends DefaultService<Integer, AuditLog, AuditLogSearchCriteria> implements AuditLogService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private AuditLogMapperExt auditLogMapperExt;

    @Override
    public ICrudGenericDAO<Integer, AuditLog> getCrudMapper() {
        return auditLogMapper;
    }

    @Override
    public ISearchableDAO<AuditLogSearchCriteria> getSearchMapper() {
        return auditLogMapperExt;
    }

    @Override
    public SimpleAuditLog findLastestLog(int auditLogId, int sAccountId) {
        return auditLogMapperExt.findLatestLog(auditLogId);
    }
}
