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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.interceptor.aspect.Auditable;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.dao.BugMapper;
import com.esofthead.mycollab.module.tracker.dao.BugMapperExt;
import com.esofthead.mycollab.module.tracker.domain.BugStatusGroupItem;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;

@Service
@Transactional
@Traceable(module = ModuleNameConstants.PRJ, nameField = "summary", type = ProjectTypeConstants.BUG, extraFieldName = "projectid")
@Auditable(module = ModuleNameConstants.PRJ, type = ProjectTypeConstants.BUG)
@Watchable(type = ProjectTypeConstants.BUG, userFieldName = "assignuser", extraTypeId = "projectid", emailHandlerBean = BugRelayEmailNotificationAction.class)
public class BugServiceImpl extends
		DefaultService<Integer, BugWithBLOBs, BugSearchCriteria> implements
		BugService {

	@Autowired
	protected BugMapper bugMapper;

	@Autowired
	protected BugMapperExt bugMapperExt;

	@Override
	public ICrudGenericDAO<Integer, BugWithBLOBs> getCrudMapper() {
		return bugMapper;
	}

	@Override
	public ISearchableDAO<BugSearchCriteria> getSearchMapper() {
		return bugMapperExt;
	}

	@Override
	public int saveWithSession(BugWithBLOBs record, String username) {
		Integer maxKey = bugMapperExt.getMaxKey(record.getProjectid());
		if (maxKey == null) {
			record.setBugkey(1);
		} else {
			record.setBugkey(maxKey + 1);
		}

		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
				ProjectGenericTaskService.class, ProjectMemberService.class,
				ProjectActivityStreamService.class);

		return super.saveWithSession(record, username);
	}

	@Override
	public int updateWithSession(BugWithBLOBs record, String username) {
		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
				ProjectActivityStreamService.class,
				ItemTimeLoggingService.class);
		return super.updateWithSession(record, username);
	}

	@Override
	public int updateSelectiveWithSession(BugWithBLOBs record, String username) {
		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
				ProjectActivityStreamService.class,
				ItemTimeLoggingService.class);
		return super.updateSelectiveWithSession(record, username);
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int accountId) {
		CacheUtils.cleanCaches(accountId, ProjectService.class,
				ProjectGenericTaskService.class, ProjectMemberService.class,
				ProjectActivityStreamService.class,
				ItemTimeLoggingService.class);
		return super.removeWithSession(primaryKey, username, accountId);
	}

	@Override
	public List<GroupItem> getStatusSummary(BugSearchCriteria criteria) {
		return bugMapperExt.getStatusSummary(criteria);
	}

	@Override
	public List<GroupItem> getPrioritySummary(BugSearchCriteria criteria) {
		return bugMapperExt.getPrioritySummary(criteria);
	}

	@Override
	public List<GroupItem> getAssignedDefectsSummary(BugSearchCriteria criteria) {
		return bugMapperExt.getAssignedDefectsSummary(criteria);
	}

	@Override
	public List<GroupItem> getReporterDefectsSummary(BugSearchCriteria criteria) {
		return bugMapperExt.getReporterDefectsSummary(criteria);
	}

	@Override
	public List<GroupItem> getResolutionDefectsSummary(
			BugSearchCriteria criteria) {
		return bugMapperExt.getResolutionDefectsSummary(criteria);
	}

	@Override
	public List<GroupItem> getComponentDefectsSummary(BugSearchCriteria criteria) {
		return bugMapperExt.getComponentDefectsSummary(criteria);
	}

	@Override
	public List<GroupItem> getVersionDefectsSummary(BugSearchCriteria criteria) {
		return bugMapperExt.getVersionDefectsSummary(criteria);
	}

	@Override
	public SimpleBug findById(int bugId, int sAccountId) {
		SimpleBug bug = bugMapperExt.getBugById(bugId);
		return bug;
	}

	@Override
	public List<BugStatusGroupItem> getBugStatusGroupItemBaseComponent(
			@CacheKey BugSearchCriteria criteria) {
		return bugMapperExt.getBugStatusGroupItemBaseComponent(criteria);
	}
}
