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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.dao.ActivityStreamMapper;
import com.esofthead.mycollab.common.dao.ActivityStreamMapperExt;
import com.esofthead.mycollab.common.domain.ActivityStream;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.common.service.ActivityStreamService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class ActivityStreamServiceImpl extends
		DefaultService<Integer, ActivityStream, ActivityStreamSearchCriteria>
		implements ActivityStreamService {

	@Autowired
	protected ActivityStreamMapper activityStreamMapper;

	@Autowired
	protected ActivityStreamMapperExt activityStreamMapperExt;

	@Override
	public ICrudGenericDAO<Integer, ActivityStream> getCrudMapper() {
		return activityStreamMapper;
	}

	@Override
	public ISearchableDAO<ActivityStreamSearchCriteria> getSearchMapper() {
		return activityStreamMapperExt;
	}

	@Override
	public Integer save(ActivityStream activityStream) {
		activityStreamMapper.insertAndReturnKey(activityStream);
		return activityStream.getId();
	}
}
