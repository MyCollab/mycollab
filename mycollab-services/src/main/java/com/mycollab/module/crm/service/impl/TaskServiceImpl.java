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
package com.mycollab.module.crm.service.impl;

import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
import com.mycollab.aspect.Watchable;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.dao.CrmTaskMapper;
import com.mycollab.module.crm.dao.CrmTaskMapperExt;
import com.mycollab.module.crm.domain.SimpleTask;
import com.mycollab.module.crm.domain.Task;
import com.mycollab.module.crm.domain.criteria.TodoSearchCriteria;
import com.mycollab.module.crm.service.EventService;
import com.mycollab.module.crm.service.TaskService;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "subject")
@Watchable(userFieldName = "assignuser")
public class TaskServiceImpl extends DefaultService<Integer, Task, TodoSearchCriteria> implements TaskService {

    static {
        ClassInfoMap.put(TaskServiceImpl.class, new ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.TASK));
    }

    @Autowired
    private CrmTaskMapper taskMapper;

    @Autowired
    private CrmTaskMapperExt taskMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, Task> getCrudMapper() {
        return taskMapper;
    }

    @Override
    public ISearchableDAO<TodoSearchCriteria> getSearchMapper() {
        return taskMapperExt;
    }

    @Override
    public SimpleTask findById(Integer taskId, Integer sAccountId) {
        return taskMapperExt.findById(taskId);
    }

    @Override
    public Integer saveWithSession(Task record, String username) {
        Integer result = super.saveWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{EventService.class}));
        return result;
    }

    @Override
    public Integer updateWithSession(Task record, String username) {
        Integer result = super.updateWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{EventService.class}));
        return result;
    }

    @Override
    public void removeByCriteria(TodoSearchCriteria criteria, Integer accountId) {
        super.removeByCriteria(criteria, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{EventService.class}));
    }

    @Override
    public void massRemoveWithSession(List<Task> tasks, String username, Integer accountId) {
        super.massRemoveWithSession(tasks, username, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{EventService.class}));
    }

    @Override
    public void massUpdateWithSession(Task record, List<Integer> primaryKeys, Integer accountId) {
        super.massUpdateWithSession(record, primaryKeys, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{EventService.class}));
    }

    @Override
    public void updateBySearchCriteria(Task record, TodoSearchCriteria searchCriteria) {
        super.updateBySearchCriteria(record, searchCriteria);
        asyncEventBus.post(new CleanCacheEvent((Integer) searchCriteria.getAccountId().getValue(),
                new Class[]{EventService.class}));
    }

}
