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
package com.mycollab.module.crm.service.impl;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
import com.mycollab.aspect.Watchable;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.dao.CaseMapper;
import com.mycollab.module.crm.dao.CaseMapperExt;
import com.mycollab.module.crm.domain.CaseWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.service.CaseService;
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
@Watchable(userFieldName = "assignuser")
public class CaseServiceImpl extends DefaultService<Integer, CaseWithBLOBs, CaseSearchCriteria> implements CaseService {
    static {
        ClassInfoMap.put(CaseServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.CASE));
    }

    @Autowired
    private CaseMapper caseMapper;

    @Autowired
    private CaseMapperExt caseMapperExt;

    @Override
    public ICrudGenericDAO<Integer, CaseWithBLOBs> getCrudMapper() {
        return caseMapper;
    }

    @Override
    public ISearchableDAO<CaseSearchCriteria> getSearchMapper() {
        return caseMapperExt;
    }

    @Override
    public SimpleCase findById(Integer caseId, Integer sAccountId) {
        return caseMapperExt.findById(caseId);
    }
}
