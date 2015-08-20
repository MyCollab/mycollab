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

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.dao.CrmTaskMapper;
import com.esofthead.mycollab.module.crm.dao.TargetMapper;
import com.esofthead.mycollab.module.crm.dao.TargetMapperExt;
import com.esofthead.mycollab.module.crm.domain.SimpleTarget;
import com.esofthead.mycollab.module.crm.domain.Target;
import com.esofthead.mycollab.module.crm.domain.criteria.TargetSearchCriteria;
import com.esofthead.mycollab.module.crm.service.TargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TargetServiceImpl extends
        DefaultService<Integer, Target, TargetSearchCriteria> implements
        TargetService {

    @Autowired
    private TargetMapper targetMapper;

    @Autowired
    private TargetMapperExt targetMapperExt;

    @Autowired
    private CrmTaskMapper taskMapper;

    @Override
    public ICrudGenericDAO<Integer, Target> getCrudMapper() {
        return targetMapper;
    }

    @Override
    public ISearchableDAO<TargetSearchCriteria> getSearchMapper() {
        return targetMapperExt;
    }

    public SimpleTarget findTargetById(int targetId) {
        return targetMapperExt.findTargetById(targetId);
    }

}
