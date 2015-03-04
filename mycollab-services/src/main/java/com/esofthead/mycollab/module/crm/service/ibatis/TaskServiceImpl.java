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

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.*;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.dao.CrmTaskMapper;
import com.esofthead.mycollab.module.crm.dao.CrmTaskMapperExt;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.domain.Task;
import com.esofthead.mycollab.module.crm.domain.criteria.TodoSearchCriteria;
import com.esofthead.mycollab.module.crm.service.EventService;
import com.esofthead.mycollab.module.crm.service.TaskService;
import com.esofthead.mycollab.schedule.email.crm.TaskRelayEmailNotificationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Transactional
@Traceable(nameField = "subject")
@Auditable()
@Watchable(userFieldName = "assignuser")
@NotifyAgent(TaskRelayEmailNotificationAction.class)
public class TaskServiceImpl extends
		DefaultService<Integer, Task, TodoSearchCriteria> implements
		TaskService {

    static {
        ClassInfoMap.put(TaskServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.TASK));
    }

	@Autowired
	private CrmTaskMapper taskMapper;

	@Autowired
	private CrmTaskMapperExt taskMapperExt;

	@Override
	public ICrudGenericDAO<Integer, Task> getCrudMapper() {
		return taskMapper;
	}

	@Override
	public ISearchableDAO<TodoSearchCriteria> getSearchMapper() {
		return taskMapperExt;
	}

	@Override
	public SimpleTask findById(int taskId, int sAccountId) {
		return taskMapperExt.findById(taskId);
	}

	@Override
	public int saveWithSession(Task record, String username) {
		int result = super.saveWithSession(record, username);
		CacheUtils.cleanCaches(record.getSaccountid(), EventService.class);
		return result;
	}

	@Override
	public int updateWithSession(Task record, String username) {
		int result = super.updateWithSession(record, username);
		CacheUtils.cleanCaches(record.getSaccountid(), EventService.class);
		return result;
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int accountId) {
		int result = super.removeWithSession(primaryKey, username, accountId);
		CacheUtils.cleanCaches(accountId, EventService.class);
		return result;
	}

	@Override
	public void removeByCriteria(TodoSearchCriteria criteria, int accountId) {
		super.removeByCriteria(criteria, accountId);
		CacheUtils.cleanCaches(accountId, EventService.class);
	}

	@Override
	public void massRemoveWithSession(List<Integer> primaryKeys,
			String username, int accountId) {
		super.massRemoveWithSession(primaryKeys, username, accountId);
		CacheUtils.cleanCaches(accountId, EventService.class);
	}

	@Override
	public void massUpdateWithSession(Task record, List<Integer> primaryKeys,
			int accountId) {
		super.massUpdateWithSession(record, primaryKeys, accountId);
		CacheUtils.cleanCaches(accountId, EventService.class);
	}

	@Override
	public void updateBySearchCriteria(Task record,
			TodoSearchCriteria searchCriteria) {
		super.updateBySearchCriteria(record, searchCriteria);
		CacheUtils.cleanCaches((Integer) searchCriteria.getAccountId()
				.getValue(), EventService.class);
	}

}
