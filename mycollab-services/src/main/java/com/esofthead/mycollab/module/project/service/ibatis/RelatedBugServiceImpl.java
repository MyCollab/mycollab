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
package com.esofthead.mycollab.module.project.service.ibatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.tracker.dao.RelatedBugMapper;
import com.esofthead.mycollab.module.tracker.dao.RelatedBugMapperExt;
import com.esofthead.mycollab.module.tracker.domain.SimpleRelatedBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugRelatedSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.RelatedBugService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class RelatedBugServiceImpl extends
		DefaultService<Integer, SimpleRelatedBug, BugRelatedSearchCriteria>
		implements RelatedBugService {

	@Autowired
	private RelatedBugMapper relatedBugMapper;

	@Autowired
	private RelatedBugMapperExt relatedTimeLoggingMapperExt;

	@Override
	public ICrudGenericDAO getCrudMapper() {
		return relatedBugMapper;
	}

	@Override
	public ISearchableDAO<BugRelatedSearchCriteria> getSearchMapper() {
		return relatedTimeLoggingMapperExt;
	}

}
