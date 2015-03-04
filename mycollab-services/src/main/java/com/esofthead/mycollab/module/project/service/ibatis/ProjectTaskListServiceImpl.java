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

import com.esofthead.mycollab.common.interceptor.aspect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.TaskListMapper;
import com.esofthead.mycollab.module.project.dao.TaskListMapperExt;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Auditable()
@Traceable(nameField = "name", extraFieldName = "projectid")
@Watchable(userFieldName = "owner", extraTypeId = "projectid")
@NotifyAgent(ProjectTaskGroupRelayEmailNotificationAction.class)
public class ProjectTaskListServiceImpl extends
		DefaultService<Integer, TaskList, TaskListSearchCriteria> implements
		ProjectTaskListService {

    static {
        ClassInfoMap.put(ProjectTaskListServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.TASK_LIST));
    }

	@Autowired
	protected TaskListMapper projectTaskListMapper;
	@Autowired
	protected TaskListMapperExt projectTaskListMapperExt;

	@Override
	public ICrudGenericDAO<Integer, TaskList> getCrudMapper() {
		return projectTaskListMapper;
	}

	@Override
	public ISearchableDAO<TaskListSearchCriteria> getSearchMapper() {
		return projectTaskListMapperExt;
	}

	@Override
	public SimpleTaskList findById(int taskListId, int sAccountId) {
		return projectTaskListMapperExt.findTaskListById(taskListId);
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int accountId) {
		CacheUtils.cleanCaches(accountId, ProjectGenericTaskService.class);
		return super.removeWithSession(primaryKey, username, accountId);
	}

	@Override
	public int saveWithSession(TaskList record, String username) {
		CacheUtils.cleanCaches(record.getSaccountid(),
				ProjectGenericTaskService.class,
				ProjectActivityStreamService.class);
		return super.saveWithSession(record, username);
	}

	@Override
	public int updateWithSession(TaskList record, String username) {
		CacheUtils.cleanCaches(record.getSaccountid(),
				ProjectGenericTaskService.class,
				ProjectActivityStreamService.class);
		return super.updateWithSession(record, username);
	}

	@Override
	public void updateTaskListIndex(TaskList[] taskLists, int sAccountId) {
		for (TaskList taskList : taskLists) {
			projectTaskListMapper.updateByPrimaryKeySelective(taskList);
		}
	}
}
