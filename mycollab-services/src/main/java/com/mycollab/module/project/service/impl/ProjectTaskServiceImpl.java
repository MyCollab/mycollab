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
package com.mycollab.module.project.service.impl;

import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.GroupItem;
import com.mycollab.common.event.TimelineTrackingAdjustIfEntityDeleteEvent;
import com.mycollab.common.event.TimelineTrackingUpdateEvent;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
import com.mycollab.aspect.Watchable;
import com.mycollab.common.service.TimelineTrackingService;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.lock.DistributionLockUtil;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.dao.TaskMapper;
import com.mycollab.module.project.dao.TaskMapperExt;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.TaskExample;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.esb.DeleteProjectTaskEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.service.*;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "taskname", extraFieldName = "projectid")
@Watchable(userFieldName = "assignuser", extraTypeId = "projectid")
public class ProjectTaskServiceImpl extends DefaultService<Integer, Task, TaskSearchCriteria> implements ProjectTaskService {
    static {
        ClassInfo taskInfo = new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.TASK);
        taskInfo.addExcludeHistoryField(Task.Field.taskindex.name());
        ClassInfoMap.put(ProjectTaskServiceImpl.class, taskInfo);
    }

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskMapperExt taskMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Autowired
    private DataSource dataSource;

    @Override
    public ICrudGenericDAO<Integer, Task> getCrudMapper() {
        return taskMapper;
    }

    @Override
    public ISearchableDAO<TaskSearchCriteria> getSearchMapper() {
        return taskMapperExt;
    }

    @Override
    public SimpleTask findById(Integer taskId, Integer sAccountId) {
        return taskMapperExt.findTaskById(taskId);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public Integer saveWithSession(Task record, String username) {
        if (record.getPercentagecomplete() == null) {
            record.setPercentagecomplete(0d);
        }
        if (record.getPercentagecomplete() == 100d) {
            record.setStatus(StatusI18nEnum.Closed.name());
        }

        if (StringUtils.isBlank(record.getStatus())) {
            record.setStatus(StatusI18nEnum.Open.name());
        }

        if (record.getPriority() == null) {
            record.setPriority(OptionI18nEnum.TaskPriority.Medium.name());
        }
        record.setLogby(username);
        Lock lock = DistributionLockUtil.getLock("task-" + record.getSaccountid());

        try {
            if (lock.tryLock(120, TimeUnit.SECONDS)) {
                Integer key = taskMapperExt.getMaxKey(record.getProjectid());
                record.setTaskkey((key == null) ? 1 : (key + 1));

                int taskId = super.saveWithSession(record, username);
                asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class, ProjectGenericTaskService.class,
                        ProjectActivityStreamService.class, ProjectMemberService.class, MilestoneService.class,
                        TimelineTrackingService.class, GanttAssignmentService.class}));
                asyncEventBus.post(new TimelineTrackingUpdateEvent(ProjectTypeConstants.TASK, taskId, "status",
                        record.getStatus(), record.getProjectid(), record.getSaccountid()));
                return taskId;
            } else {
                throw new MyCollabException("Timeout operation.");
            }
        } catch (InterruptedException e) {
            throw new MyCollabException(e);
        } finally {
            DistributionLockUtil.removeLock("task-" + record.getSaccountid());
            lock.unlock();
        }
    }

    @Transactional
    @Override
    public Integer updateWithSession(Task record, String username) {
        beforeUpdate(record);
        int result = super.updateWithSession(record, username);
        cleanCacheUpdate(record);
        return result;
    }

    private void beforeUpdate(Task record) {
        if ((record.getPercentagecomplete() != null) && (record.getPercentagecomplete() == 100d)) {
            record.setStatus(StatusI18nEnum.Closed.name());
        } else if (record.getStatus() == null) {
            record.setStatus(StatusI18nEnum.Open.name());
        } else if (StatusI18nEnum.Closed.name().equals(record.getStatus())) {
            record.setPercentagecomplete(100d);
        }
    }

    @Override
    public Integer updateSelectiveWithSession(Task record, String username) {
        beforeUpdate(record);
        int result = super.updateSelectiveWithSession(record, username);
        cleanCacheUpdate(record);
        return result;
    }

    private void cleanCacheUpdate(Task record) {
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class, ProjectMemberService.class,
                MilestoneService.class, ItemTimeLoggingService.class, TimelineTrackingService.class,
                GanttAssignmentService.class}));
        asyncEventBus.post(new TimelineTrackingUpdateEvent(ProjectTypeConstants.TASK, record.getId(), "status",
                record.getStatus(), record.getProjectid(), record.getSaccountid()));
    }

    @Override
    public void massRemoveWithSession(List<Task> items, String username, Integer accountId) {
        super.massRemoveWithSession(items, username, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class, ProjectMemberService.class,
                MilestoneService.class, ItemTimeLoggingService.class, GanttAssignmentService.class}));
        DeleteProjectTaskEvent event = new DeleteProjectTaskEvent(items.toArray(new Task[items.size()]),
                username, accountId);
        asyncEventBus.post(event);
    }

    @Override
    public void removeWithSession(Task item, String username, Integer accountId) {
        super.removeWithSession(item, username, accountId);
        asyncEventBus.post(new TimelineTrackingAdjustIfEntityDeleteEvent(ProjectTypeConstants.TASK, item.getId(), new
                String[]{"status"}, item.getProjectid(), item.getSaccountid()));
    }

    @Override
    public List<GroupItem> getPrioritySummary(TaskSearchCriteria criteria) {
        return taskMapperExt.getPrioritySummary(criteria);
    }

    @Override
    public List<GroupItem> getStatusSummary(@CacheKey TaskSearchCriteria criteria) {
        return taskMapperExt.getStatusSummary(criteria);
    }

    @Override
    public List<GroupItem> getAssignedTasksSummary(TaskSearchCriteria criteria) {
        return taskMapperExt.getAssignedDefectsSummary(criteria);
    }

    @Override
    public SimpleTask findByProjectAndTaskKey(Integer taskKey, String projectShortName, Integer sAccountId) {
        return taskMapperExt.findByProjectAndTaskKey(taskKey, projectShortName, sAccountId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SimpleTask> findSubTasks(Integer parentTaskId, Integer sAccountId, SearchCriteria.OrderField orderField) {
        TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(sAccountId));
        searchCriteria.setParentTaskId(new NumberSearchField(parentTaskId));
        searchCriteria.setOrderFields(Collections.singletonList(orderField));
        return taskMapperExt.findPageableListByCriteria(searchCriteria, new RowBounds(0, Integer.MAX_VALUE));
    }

    @Override
    public void massUpdateTaskIndexes(final List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate("UPDATE `m_prj_task` SET `taskindex`=? WHERE `id`=?", new
                BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, mapIndexes.get(i).get("index"));
                        preparedStatement.setInt(2, mapIndexes.get(i).get("id"));
                    }

                    @Override
                    public int getBatchSize() {
                        return mapIndexes.size();
                    }
                });
    }

    @Override
    public void massUpdateStatuses(String oldStatus, String newStatus, Integer projectId, @CacheKey Integer sAccountId) {
        Task updateTaskStatus = new Task();
        updateTaskStatus.setStatus(newStatus);
        TaskExample ex = new TaskExample();
        ex.createCriteria().andStatusEqualTo(oldStatus).andProjectidEqualTo(projectId).andSaccountidEqualTo(sAccountId);
        taskMapper.updateByExampleSelective(updateTaskStatus, ex);
    }
}
