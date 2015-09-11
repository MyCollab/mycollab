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

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.lock.DistributionLockUtil;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.esb.DeleteProjectBugEvent;
import com.esofthead.mycollab.module.project.service.*;
import com.esofthead.mycollab.module.tracker.dao.BugMapper;
import com.esofthead.mycollab.module.tracker.dao.BugMapperExt;
import com.esofthead.mycollab.module.tracker.domain.BugStatusGroupItem;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
@Transactional
@Traceable(nameField = "summary", extraFieldName = "projectid", notifyAgent = BugRelayEmailNotificationAction.class)
public class BugServiceImpl extends DefaultService<Integer, BugWithBLOBs, BugSearchCriteria> implements BugService {
    static {
        ClassInfo bugInfo = new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG);
        bugInfo.addExcludeHistoryField(BugWithBLOBs.Field.bugindex.name());
        ClassInfoMap.put(BugServiceImpl.class, bugInfo);
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BugMapper bugMapper;

    @Autowired
    private BugMapperExt bugMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, BugWithBLOBs> getCrudMapper() {
        return bugMapper;
    }

    @Override
    public ISearchableDAO<BugSearchCriteria> getSearchMapper() {
        return bugMapperExt;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public Integer saveWithSession(BugWithBLOBs record, String username) {
        Lock lock = DistributionLockUtil.getLock("bug-" + record.getSaccountid());
        try {
            if (lock.tryLock(120, TimeUnit.SECONDS)) {
                Integer maxKey = bugMapperExt.getMaxKey(record.getProjectid());
                record.setBugkey((maxKey == null) ? 1 : (maxKey + 1));
                CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class, ProjectGenericTaskService.class,
                        ProjectMemberService.class, ProjectActivityStreamService.class);
                return super.saveWithSession(record, username);
            } else {
                throw new MyCollabException("Timeout operation");
            }
        } catch (InterruptedException e) {
            throw new MyCollabException(e);
        } finally {
            DistributionLockUtil.removeLock("bug-" + record.getSaccountid());
            lock.unlock();
        }
    }

    @Override
    public Integer updateWithSession(BugWithBLOBs record, String username) {
        CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
                ProjectActivityStreamService.class, ItemTimeLoggingService.class);
        return super.updateWithSession(record, username);
    }

    @Override
    public Integer updateSelectiveWithSession(BugWithBLOBs record, String username) {
        CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
                ProjectActivityStreamService.class, ItemTimeLoggingService.class);
        return super.updateSelectiveWithSession(record, username);
    }

    @Override
    public void massRemoveWithSession(List<BugWithBLOBs> items, String username, Integer accountId) {
        super.massRemoveWithSession(items, username, accountId);
        CacheUtils.cleanCaches(accountId, ProjectService.class, ItemTimeLoggingService.class);
        DeleteProjectBugEvent event = new DeleteProjectBugEvent(items.toArray(new BugWithBLOBs[items.size()]),
                username, accountId);
        asyncEventBus.post(event);
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
    public List<GroupItem> getResolutionDefectsSummary(BugSearchCriteria criteria) {
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
    public SimpleBug findById(Integer bugId, Integer sAccountId) {
        return bugMapperExt.getBugById(bugId);
    }

    @Override
    public List<BugStatusGroupItem> getBugStatusGroupItemBaseComponent(BugSearchCriteria criteria) {
        return bugMapperExt.getBugStatusGroupItemBaseComponent(criteria);
    }

    @Override
    public SimpleBug findByProjectAndBugKey(Integer bugKey, String projectShortName, Integer sAccountId) {
        return bugMapperExt.findByProjectAndBugKey(bugKey, projectShortName, sAccountId);
    }

    @Override
    public void massUpdateBugIndexes(final List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate("UPDATE `m_tracker_bug` SET `bugIndex`=? WHERE `id`=?", new
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
}
