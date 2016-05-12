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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.lock.DistributionLockUtil;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.GanttMapperExt;
import com.esofthead.mycollab.module.project.dao.MilestoneMapper;
import com.esofthead.mycollab.module.project.dao.PredecessorMapper;
import com.esofthead.mycollab.module.project.dao.TaskMapper;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.module.tracker.dao.BugMapper;
import com.esofthead.mycollab.module.tracker.domain.BugExample;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.google.common.base.MoreObjects;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class GanttAssignmentServiceImpl implements GanttAssignmentService {

    @Autowired
    private GanttMapperExt ganttMapperExt;

    @Autowired
    private DataSource dataSource;

    @Override
    public List<AssignWithPredecessors> getTaskWithPredecessors(List<Integer> projectIds, @CacheKey Integer sAccountId) {
        return ganttMapperExt.getTaskWithPredecessors(projectIds, sAccountId);
    }

    @Override
    public void massUpdateGanttItems(final List<AssignWithPredecessors> ganttItems, Integer sAccountId) {
        if (CollectionUtils.isNotEmpty(ganttItems)) {
            List<MilestoneGanttItem> milestoneGanttItems = new ArrayList<>();
            List<TaskGanttItem> taskGanttItems = new ArrayList<>();
            List<TaskGanttItem> bugGanttItems = new ArrayList<>();

            for (AssignWithPredecessors ganttItem : ganttItems) {
                if (ganttItem instanceof MilestoneGanttItem) {
                    milestoneGanttItems.add((MilestoneGanttItem) ganttItem);
                } else if (ganttItem instanceof TaskGanttItem) {
                    if (ProjectTypeConstants.BUG.equals(ganttItem.getType())) {
                        bugGanttItems.add((TaskGanttItem) ganttItem);
                    } else if (ProjectTypeConstants.TASK.equals(ganttItem.getType())) {
                        taskGanttItems.add((TaskGanttItem) ganttItem);
                    }
                } else {
                    throw new MyCollabException("Do not support save gantt item " + ganttItem);
                }
            }
            massUpdateMilestoneGanttItems(milestoneGanttItems, sAccountId);
            massUpdateTaskGanttItems(taskGanttItems, sAccountId);
            massUpdateBugGanttItems(bugGanttItems, sAccountId);
        }
    }

    @Override
    public void massUpdatePredecessors(Integer taskSourceId, final List<TaskPredecessor> predecessors, Integer sAccountId) {
        Lock lock = DistributionLockUtil.getLock("task-service" + sAccountId);
        try {
            PredecessorMapper predecessorMapper = AppContextUtil.getSpringBean(PredecessorMapper.class);
            PredecessorExample ex = new PredecessorExample();
            ex.createCriteria().andSourceidEqualTo(taskSourceId);
            predecessorMapper.deleteByExample(ex);

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            final long now = new GregorianCalendar().getTimeInMillis();
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                jdbcTemplate.batchUpdate("INSERT INTO `m_prj_predecessor`(`sourceType`, `descType`, `predestype`,`lagDay`, " +
                                "`sourceId`,`descId`, `createdTime`) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                                preparedStatement.setString(1, predecessors.get(i).getSourcetype());
                                preparedStatement.setString(2, predecessors.get(i).getDesctype());
                                preparedStatement.setString(3, predecessors.get(i).getPredestype());
                                preparedStatement.setInt(4, predecessors.get(i).getLagday());
                                preparedStatement.setInt(5, predecessors.get(i).getSourceid());
                                preparedStatement.setInt(6, predecessors.get(i).getDescid());
                                preparedStatement.setDate(7, new Date(now));
                            }

                            @Override
                            public int getBatchSize() {
                                return predecessors.size();
                            }
                        });
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        } finally {
            DistributionLockUtil.removeLock("task-service" + sAccountId);
            lock.unlock();
        }
    }

    @Override
    public void massDeletePredecessors(List<TaskPredecessor> predecessors, @CacheKey Integer sAccountId) {
        Lock lock = DistributionLockUtil.getLock("gantt-predecessor-service" + sAccountId);
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                try (Connection connection = dataSource.getConnection()) {
                    connection.setAutoCommit(false);
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " +
                            "`m_prj_predecessor` WHERE sourceType=? AND predestype=? AND sourceId=? AND descId=? AND descType=?");
                    for (int i = 0; i < predecessors.size(); i++) {
                        preparedStatement.setString(1, predecessors.get(i).getSourcetype());
                        preparedStatement.setString(2, predecessors.get(i).getPredestype());
                        preparedStatement.setInt(3, predecessors.get(i).getSourceid());
                        preparedStatement.setInt(4, predecessors.get(i).getDescid());
                        preparedStatement.setString(5, predecessors.get(i).getDesctype());
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                    connection.commit();
                }
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        } finally {
            DistributionLockUtil.removeLock("gantt-predecessor-service" + sAccountId);
            lock.unlock();
        }
    }

    private void massUpdateMilestoneGanttItems(final List<MilestoneGanttItem> milestoneGanttItems, Integer sAccountId) {
        if (CollectionUtils.isNotEmpty(milestoneGanttItems)) {
            Lock lock = DistributionLockUtil.getLock("gantt-milestone-service" + sAccountId);
            try {
                final long now = new GregorianCalendar().getTimeInMillis();
                if (lock.tryLock(30, TimeUnit.SECONDS)) {
                    try (Connection connection = dataSource.getConnection()) {
                        connection.setAutoCommit(false);
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `m_prj_milestone` SET " +
                                "name = ?, `startdate` = ?, `enddate` = ?, " +
                                "`lastUpdatedTime`=?, `owner`=?, `ganttIndex`=? WHERE `id` = ?");
                        for (int i = 0; i < milestoneGanttItems.size(); i++) {
                            preparedStatement.setString(1, milestoneGanttItems.get(i).getName());
                            preparedStatement.setDate(2, getDateWithNullValue(milestoneGanttItems.get(i).getStartDate()));
                            preparedStatement.setDate(3, getDateWithNullValue(milestoneGanttItems.get(i).getEndDate()));
                            preparedStatement.setDate(4, new Date(now));
                            preparedStatement.setString(5, milestoneGanttItems.get(i).getAssignUser());
                            preparedStatement.setInt(6, milestoneGanttItems.get(i).getGanttIndex());
                            preparedStatement.setInt(7, milestoneGanttItems.get(i).getId());
                            preparedStatement.addBatch();

                        }
                        preparedStatement.executeBatch();
                        connection.commit();
                    }
                }
            } catch (Exception e) {
                throw new MyCollabException(e);
            } finally {
                DistributionLockUtil.removeLock("gantt-milestone-service" + sAccountId);
                lock.unlock();
            }
        }
    }

    private void massUpdateTaskGanttItems(final List<TaskGanttItem> taskGanttItems, Integer sAccountId) {
        if (CollectionUtils.isNotEmpty(taskGanttItems)) {
            Lock lock = DistributionLockUtil.getLock("gantt-task-service" + sAccountId);
            try {
                final long now = new GregorianCalendar().getTimeInMillis();
                if (lock.tryLock(30, TimeUnit.SECONDS)) {
                    try (Connection connection = dataSource.getConnection()) {
                        connection.setAutoCommit(false);
                        PreparedStatement batchTasksStatement = connection.prepareStatement("UPDATE `m_prj_task` SET " +
                                "taskname = ?, `startdate` = ?, `enddate` = ?, " +
                                "`lastUpdatedTime`=?, `percentagecomplete`=?, `assignUser`=?, `ganttindex`=?, " +
                                "`milestoneId`=?, `parentTaskId`=? WHERE `id` = ?");
                        for (int i = 0; i < taskGanttItems.size(); i++) {
                            TaskGanttItem ganttItem = taskGanttItems.get(i);
                            if (ProjectTypeConstants.TASK.equals(ganttItem.getType())) {
                                batchTasksStatement.setString(1, ganttItem.getName());
                                batchTasksStatement.setDate(2, getDateWithNullValue(ganttItem.getStartDate()));
                                batchTasksStatement.setDate(3, getDateWithNullValue(ganttItem.getEndDate()));
                                batchTasksStatement.setDate(4, new Date(now));
                                batchTasksStatement.setDouble(5, ganttItem.getProgress());
                                batchTasksStatement.setString(6, ganttItem.getAssignUser());
                                batchTasksStatement.setInt(7, ganttItem.getGanttIndex());
                                batchTasksStatement.setObject(8, ganttItem.getMilestoneId());
                                batchTasksStatement.setObject(9, ganttItem.getParentTaskId());
                                batchTasksStatement.setInt(10, ganttItem.getId());
                                batchTasksStatement.addBatch();
                            }

                        }
                        batchTasksStatement.executeBatch();
                        connection.commit();
                    }
                }
            } catch (Exception e) {
                throw new MyCollabException(e);
            } finally {
                DistributionLockUtil.removeLock("gantt-task-service" + sAccountId);
                lock.unlock();
            }
        }
    }

    private void massUpdateBugGanttItems(final List<TaskGanttItem> taskGanttItems, Integer sAccountId) {
        if (CollectionUtils.isNotEmpty(taskGanttItems)) {
            Lock lock = DistributionLockUtil.getLock("gantt-bug-service" + sAccountId);
            try {
                final long now = new GregorianCalendar().getTimeInMillis();
                if (lock.tryLock(30, TimeUnit.SECONDS)) {
                    try (Connection connection = dataSource.getConnection()) {
                        connection.setAutoCommit(false);
                        PreparedStatement batchTasksStatement = connection.prepareStatement("UPDATE `m_tracker_bug` SET " +
                                "summary = ?, `startdate` = ?, `enddate` = ?, " +
                                "`lastUpdatedTime`=?, `percentagecomplete`=?, `assignuser`=?, `ganttindex`=?, " +
                                "`milestoneId`=? WHERE `id` = ?");
                        for (int i = 0; i < taskGanttItems.size(); i++) {
                            TaskGanttItem ganttItem = taskGanttItems.get(i);
                            if (ProjectTypeConstants.BUG.equals(ganttItem.getType())) {
                                batchTasksStatement.setString(1, ganttItem.getName());
                                batchTasksStatement.setDate(2, getDateWithNullValue(ganttItem.getStartDate()));
                                batchTasksStatement.setDate(3, getDateWithNullValue(ganttItem.getEndDate()));
                                batchTasksStatement.setDate(4, new Date(now));
                                batchTasksStatement.setDouble(5, MoreObjects.firstNonNull(ganttItem.getProgress(), 0d));
                                batchTasksStatement.setString(6, ganttItem.getAssignUser());
                                batchTasksStatement.setInt(7, ganttItem.getGanttIndex());
                                batchTasksStatement.setObject(8, ganttItem.getMilestoneId());
                                batchTasksStatement.setInt(9, ganttItem.getId());
                                batchTasksStatement.addBatch();
                            }

                        }
                        batchTasksStatement.executeBatch();
                        connection.commit();
                    }
                }
            } catch (Exception e) {
                throw new MyCollabException(e);
            } finally {
                DistributionLockUtil.removeLock("gantt-bug-service" + sAccountId);
                lock.unlock();
            }
        }
    }

    @Override
    public void massDeleteGanttItems(List<AssignWithPredecessors> ganttItems, Integer sAccountId) {
        if (CollectionUtils.isNotEmpty(ganttItems)) {
            List<Integer> milestoneIds = new ArrayList<>();
            List<Integer> taskIds = new ArrayList<>();
            List<Integer> bugIds = new ArrayList<>();

            for (AssignWithPredecessors ganttItem : ganttItems) {
                if (ganttItem instanceof MilestoneGanttItem) {
                    if (ganttItem.getId() != null) {
                        milestoneIds.add(ganttItem.getId());
                    }

                } else if (ganttItem instanceof TaskGanttItem) {
                    if (ProjectTypeConstants.TASK.equals(ganttItem.getType()) && ganttItem.getId() != null) {
                        taskIds.add(ganttItem.getId());
                    } else if (ProjectTypeConstants.BUG.equals(ganttItem.getType()) && ganttItem.getId() != null) {
                        bugIds.add(ganttItem.getId());
                    }
                } else {
                    throw new MyCollabException("Do not support delete gantt item " + ganttItem);
                }
            }
            massDeleteMilestoneGanttItems(milestoneIds);
            massDeleteTaskGanttItems(taskIds);
            massDeleteBugGanttItems(bugIds);
        }
    }

    private void massDeleteMilestoneGanttItems(List<Integer> milestoneIds) {
        if (CollectionUtils.isNotEmpty(milestoneIds)) {
            MilestoneMapper milestoneMapper = AppContextUtil.getSpringBean(MilestoneMapper.class);
            MilestoneExample ex = new MilestoneExample();
            ex.createCriteria().andIdIn(milestoneIds);
            milestoneMapper.deleteByExample(ex);
        }
    }

    private void massDeleteTaskGanttItems(List<Integer> taskIds) {
        if (CollectionUtils.isNotEmpty(taskIds)) {
            TaskMapper taskMapper = AppContextUtil.getSpringBean(TaskMapper.class);
            TaskExample ex = new TaskExample();
            ex.createCriteria().andIdIn(taskIds);
            taskMapper.deleteByExample(ex);
        }
    }

    private void massDeleteBugGanttItems(List<Integer> bugIds) {
        if (CollectionUtils.isNotEmpty(bugIds)) {
            BugMapper bugMapper = AppContextUtil.getSpringBean(BugMapper.class);
            BugExample ex = new BugExample();
            ex.createCriteria().andIdIn(bugIds);
            bugMapper.deleteByExample(ex);
        }
    }

    private static Date getDateWithNullValue(java.util.Date date) {
        return (date != null) ? new Date(date.getTime()) : null;
    }
}
