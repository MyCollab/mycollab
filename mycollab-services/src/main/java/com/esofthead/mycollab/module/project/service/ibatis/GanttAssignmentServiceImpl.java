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
import com.esofthead.mycollab.module.project.dao.GanttMapperExt;
import com.esofthead.mycollab.module.project.dao.MilestoneMapper;
import com.esofthead.mycollab.module.project.dao.TaskMapper;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
            for (AssignWithPredecessors ganttItem : ganttItems) {
                if (ganttItem instanceof MilestoneGanttItem) {
                    milestoneGanttItems.add((MilestoneGanttItem) ganttItem);
                } else if (ganttItem instanceof TaskGanttItem) {
                    taskGanttItems.add((TaskGanttItem) ganttItem);
                } else {
                    throw new MyCollabException("Do not support save gantt item " + ganttItem);
                }
            }
            massUpdateMilestoneGanttItems(milestoneGanttItems, sAccountId);
            massUpdateTaskGanttItems(taskGanttItems, sAccountId);
        }

    }

    private void massUpdateMilestoneGanttItems(final List<MilestoneGanttItem> milestoneGanttItems, Integer sAccountId) {
        if (CollectionUtils.isNotEmpty(milestoneGanttItems)) {
            Lock lock = DistributionLockUtil.getLock("gantt-task-service" + sAccountId);
            try {
                final long now = new GregorianCalendar().getTimeInMillis();
                if (lock.tryLock(30, TimeUnit.SECONDS)) {
                    try (Connection connection = dataSource.getConnection()) {
                        connection.setAutoCommit(false);
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `m_prj_milestone` SET " +
                                "name = ?, `startdate` = ?, `enddate` = ?, `actualstartdate`=?,`actualenddate`=?, " +
                                "`lastUpdatedTime`=?, `owner`=?, `ganttIndex`=? WHERE `id` = ?");
                        for (int i = 0; i < milestoneGanttItems.size(); i++) {
                            preparedStatement.setString(1, milestoneGanttItems.get(i).getName());
                            preparedStatement.setDate(2, getDateWithNullValue(milestoneGanttItems.get(i).getStartDate()));
                            preparedStatement.setDate(3, getDateWithNullValue(milestoneGanttItems.get(i).getEndDate()));
                            preparedStatement.setDate(4, getDateWithNullValue(milestoneGanttItems.get(i).getActualStartDate()));
                            preparedStatement.setDate(5, getDateWithNullValue(milestoneGanttItems.get(i).getActualEndDate()));
                            preparedStatement.setDate(6, new Date(now));
                            preparedStatement.setString(7, milestoneGanttItems.get(i).getAssignUser());
                            preparedStatement.setInt(8, milestoneGanttItems.get(i).getGanttIndex());
                            preparedStatement.setInt(9, milestoneGanttItems.get(i).getId());
                            preparedStatement.addBatch();

                        }
                        preparedStatement.executeBatch();
                        connection.commit();
                    }
                }
            } catch (Exception e) {
                throw new MyCollabException(e);
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
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `m_prj_task` SET " +
                                "taskname = ?, `startdate` = ?, `enddate` = ?, `actualStartDate`=?, `actualEndDate`=?, " +
                                "`lastUpdatedTime`=?, `percentagecomplete`=?, `assignUser`=?, `ganttindex`=?, " +
                                "`milestoneId`=?, `parentTaskId`=? WHERE `id` = ?");
                        for (int i = 0; i < taskGanttItems.size(); i++) {
                            preparedStatement.setString(1, taskGanttItems.get(i).getName());
                            preparedStatement.setDate(2, getDateWithNullValue(taskGanttItems.get(i).getStartDate()));
                            preparedStatement.setDate(3, getDateWithNullValue(taskGanttItems.get(i).getEndDate()));
                            preparedStatement.setDate(4, getDateWithNullValue(taskGanttItems.get(i).getActualStartDate()));
                            preparedStatement.setDate(5, getDateWithNullValue(taskGanttItems.get(i).getActualEndDate()));
                            preparedStatement.setDate(6, new Date(now));
                            preparedStatement.setDouble(7, taskGanttItems.get(i).getProgress());
                            preparedStatement.setString(8, taskGanttItems.get(i).getAssignUser());
                            preparedStatement.setInt(9, taskGanttItems.get(i).getGanttIndex());
                            preparedStatement.setObject(10, taskGanttItems.get(i).getMilestoneId());
                            preparedStatement.setObject(11, taskGanttItems.get(i).getParentTaskId());
                            preparedStatement.setInt(12, taskGanttItems.get(i).getId());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                        connection.commit();
                    }
                }
            } catch (Exception e) {
                throw new MyCollabException(e);
            }
        }
    }

    @Override
    public void massDeleteGanttItems(List<AssignWithPredecessors> ganttItems, Integer sAccountId) {
        if (CollectionUtils.isNotEmpty(ganttItems)) {
            List<Integer> milestoneIds = new ArrayList<>();
            List<Integer> taskIds = new ArrayList<>();
            for (AssignWithPredecessors ganttItem : ganttItems) {
                if (ganttItem instanceof MilestoneGanttItem) {
                    if (ganttItem.getId() != null) {
                        milestoneIds.add(ganttItem.getId());
                    }

                } else if (ganttItem instanceof TaskGanttItem) {
                    if (ganttItem.getId() != null) {
                        taskIds.add(ganttItem.getId());
                    }
                } else {
                    throw new MyCollabException("Do not support delete gantt item " + ganttItem);
                }
            }
            massDeleteMilestoneGanttItems(milestoneIds);
            massDeleteTaskGanttItems(taskIds);
        }
    }

    private void massDeleteMilestoneGanttItems(List<Integer> milestoneIds) {
        if (CollectionUtils.isNotEmpty(milestoneIds)) {
            MilestoneMapper milestoneMapper = ApplicationContextUtil.getSpringBean(MilestoneMapper.class);
            MilestoneExample ex = new MilestoneExample();
            ex.createCriteria().andIdIn(milestoneIds);
            milestoneMapper.deleteByExample(ex);
        }
    }

    private void massDeleteTaskGanttItems(List<Integer> taskIds) {
        if (CollectionUtils.isNotEmpty(taskIds)) {
            TaskMapper taskMapper = ApplicationContextUtil.getSpringBean(TaskMapper.class);
            TaskExample ex = new TaskExample();
            ex.createCriteria().andIdIn(taskIds);
            taskMapper.deleteByExample(ex);
        }
    }

    private static Date getDateWithNullValue(java.util.Date date) {
        return (date != null) ? new Date(date.getTime()) : null;
    }
}
