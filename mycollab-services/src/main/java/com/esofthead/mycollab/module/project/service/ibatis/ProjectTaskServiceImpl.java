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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.common.interceptor.aspect.Auditable;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.TaskMapper;
import com.esofthead.mycollab.module.project.dao.TaskMapperExt;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Transactional
@Traceable(module = ModuleNameConstants.PRJ, type = ProjectTypeConstants.TASK, nameField = "taskname", extraFieldName = "projectid")
@Auditable(module = ModuleNameConstants.PRJ, type = ProjectTypeConstants.TASK)
@Watchable(type = ProjectTypeConstants.TASK, userFieldName = "assignuser", extraTypeId = "projectid", emailHandlerBean = ProjectTaskRelayEmailNotificationAction.class)
public class ProjectTaskServiceImpl extends
		DefaultService<Integer, Task, TaskSearchCriteria> implements
		ProjectTaskService {

	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private TaskMapperExt taskMapperExt;

	@Override
	public ICrudGenericDAO<Integer, Task> getCrudMapper() {
		return taskMapper;
	}

	@Override
	public ISearchableDAO<TaskSearchCriteria> getSearchMapper() {
		return taskMapperExt;
	}

	@Override
	public SimpleTask findById(int taskId, int sAccountId) {
		return taskMapperExt.findTaskById(taskId);
	}

	@Override
	public int saveWithSession(Task record, String username) {
		if ((record.getPercentagecomplete() != null)
				&& (record.getPercentagecomplete() == 100)) {
			record.setStatus(StatusI18nEnum.Closed.name());
		} else {
			record.setStatus(StatusI18nEnum.Open.name());
		}
		record.setLogby(username);

		Integer key = taskMapperExt.getMaxKey(record.getProjectid());
		record.setTaskkey((key == null) ? 1 : (key + 1));

		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
				ProjectGenericTaskService.class, ProjectTaskListService.class,
				ProjectActivityStreamService.class, ProjectMemberService.class,
				MilestoneService.class);

		return super.saveWithSession(record, username);
	}

	@Override
	public int updateWithSession(Task record, String username) {
		beforeUpdate(record);

		return super.updateWithSession(record, username);
	}
	
	private void beforeUpdate(Task record) {
		if ((record.getPercentagecomplete() != null)
				&& (record.getPercentagecomplete() == 100)) {
			record.setStatus(StatusI18nEnum.Closed.name());
		} else if (record.getStatus() == null) {
			record.setStatus(StatusI18nEnum.Open.name());
		}
		
		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
				ProjectGenericTaskService.class, ProjectTaskListService.class,
				ProjectActivityStreamService.class, ProjectMemberService.class,
				MilestoneService.class, ItemTimeLoggingService.class);
	}

	@Override
	public int updateSelectiveWithSession(Task record, String username) {
		beforeUpdate(record);
		return super.updateSelectiveWithSession(record, username);
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int accountId) {
		int result = super.removeWithSession(primaryKey, username, accountId);
		CacheUtils.cleanCaches(accountId, ProjectTaskListService.class,
				ProjectService.class, ProjectGenericTaskService.class,
				ProjectActivityStreamService.class, MilestoneService.class,
				ItemTimeLoggingService.class);

		return result;
	}

	@Override
	public List<GroupItem> getPrioritySummary(TaskSearchCriteria criteria) {
		return taskMapperExt.getPrioritySummary(criteria);
	}

	@Override
	public List<GroupItem> getAssignedDefectsSummary(TaskSearchCriteria criteria) {
		return taskMapperExt.getAssignedDefectsSummary(criteria);
	}

}
