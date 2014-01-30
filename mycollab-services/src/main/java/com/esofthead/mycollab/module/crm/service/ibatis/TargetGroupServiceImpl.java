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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.dao.TargetGroupMapper;
import com.esofthead.mycollab.module.crm.dao.TargetGroupMapperExt;
import com.esofthead.mycollab.module.crm.domain.TargetGroup;
import com.esofthead.mycollab.module.crm.domain.criteria.TargetGroupSearchCriteria;
import com.esofthead.mycollab.module.crm.service.TargetGroupService;

@Service
@Transactional
public class TargetGroupServiceImpl extends
		DefaultService<Integer, TargetGroup, TargetGroupSearchCriteria> implements TargetGroupService {

	@Autowired
	private TargetGroupMapper targetGroupMapper;
	
	@Autowired
	private TargetGroupMapperExt targetGroupMapperExt;

	@Override
	public ICrudGenericDAO<Integer, TargetGroup> getCrudMapper() {
		return targetGroupMapper;
	}

	@Override
	public ISearchableDAO<TargetGroupSearchCriteria> getSearchMapper() {
		return targetGroupMapperExt;
	}

}
