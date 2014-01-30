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
package com.esofthead.mycollab.module.ecm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.ecm.dao.ContentActivityLogMapper;
import com.esofthead.mycollab.module.ecm.dao.ContentActivityLogMapperExt;
import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogWithBLOBs;
import com.esofthead.mycollab.module.ecm.domain.criteria.ContentActivityLogSearchCriteria;
import com.esofthead.mycollab.module.ecm.service.ContentActivityLogService;

@Service
public class ContentActivityLogServiceImpl
		extends
		DefaultService<Integer, ContentActivityLogWithBLOBs, ContentActivityLogSearchCriteria>
		implements ContentActivityLogService {

	@Autowired
	private ContentActivityLogMapper contentActivityLogMapper;

	@Autowired
	private ContentActivityLogMapperExt contentActivityLogMapperExt;

	@Override
	public ICrudGenericDAO getCrudMapper() {
		return contentActivityLogMapper;
	}

	@Override
	public ISearchableDAO<ContentActivityLogSearchCriteria> getSearchMapper() {
		return contentActivityLogMapperExt;
	}

}
