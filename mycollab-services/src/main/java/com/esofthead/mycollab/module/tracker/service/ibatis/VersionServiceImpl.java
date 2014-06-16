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
package com.esofthead.mycollab.module.tracker.service.ibatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.Auditable;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.tracker.dao.VersionMapper;
import com.esofthead.mycollab.module.tracker.dao.VersionMapperExt;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.schedule.email.project.VersionRelayEmailNotificationAction;

@Service
@Transactional
@Traceable(module = ModuleNameConstants.PRJ, nameField = "versionname", type = ProjectTypeConstants.BUG_VERSION, extraFieldName = "projectid")
@Auditable(module = ModuleNameConstants.PRJ, type = ProjectTypeConstants.BUG_VERSION)
@Watchable(type = ProjectTypeConstants.BUG_VERSION, extraTypeId = "projectid", emailHandlerBean = VersionRelayEmailNotificationAction.class)
public class VersionServiceImpl extends
		DefaultService<Integer, Version, VersionSearchCriteria> implements
		VersionService {

	@Autowired
	private VersionMapper versionMapper;

	@Autowired
	private VersionMapperExt versionMapperExt;

	@Override
	public ICrudGenericDAO<Integer, Version> getCrudMapper() {
		return versionMapper;
	}

	@Override
	public ISearchableDAO<VersionSearchCriteria> getSearchMapper() {
		return versionMapperExt;
	}

	@Override
	public SimpleVersion findById(int versionId, int sAccountId) {
		return versionMapperExt.findVersionById(versionId);
	}
}
