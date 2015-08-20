/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common.service.ibatis;

import com.esofthead.mycollab.common.dao.AuditLogMapper;
import com.esofthead.mycollab.common.dao.AuditLogMapperExt;
import com.esofthead.mycollab.common.domain.AuditLog;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class AuditLogServiceImpl extends DefaultService<Integer, AuditLog, AuditLogSearchCriteria> implements AuditLogService {

    @Autowired
    protected AuditLogMapper auditLogMapper;
    @Autowired
    protected AuditLogMapperExt auditLogMapperExt;

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
