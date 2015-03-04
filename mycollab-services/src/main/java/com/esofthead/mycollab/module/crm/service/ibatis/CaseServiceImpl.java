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
package com.esofthead.mycollab.module.crm.service.ibatis;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.*;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.CaseMapper;
import com.esofthead.mycollab.module.crm.dao.CaseMapperExt;
import com.esofthead.mycollab.module.crm.domain.CaseWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.schedule.email.crm.CaseRelayEmailNotificationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "subject")
@Auditable()
@Watchable(userFieldName = "assignuser")
@NotifyAgent(CaseRelayEmailNotificationAction.class)
public class CaseServiceImpl extends
        DefaultService<Integer, CaseWithBLOBs, CaseSearchCriteria> implements
        CaseService {
    static {
        ClassInfoMap.put(CaseServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.CASE));
    }

    @Autowired
    protected CaseMapper caseMapper;
    @Autowired
    protected CaseMapperExt caseMapperExt;

    @Override
    public ICrudGenericDAO<Integer, CaseWithBLOBs> getCrudMapper() {
        return caseMapper;
    }

    @Override
    public ISearchableDAO<CaseSearchCriteria> getSearchMapper() {
        return caseMapperExt;
    }

    @Override
    public SimpleCase findById(int caseId, int sAccountId) {
        return caseMapperExt.findById(caseId);
    }
}
