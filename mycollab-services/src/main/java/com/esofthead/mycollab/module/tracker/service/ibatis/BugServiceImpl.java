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
import com.esofthead.mycollab.common.interceptor.aspect.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
@Transactional
@Traceable(nameField = "summary", extraFieldName = "projectid")
@Auditable()
@NotifyAgent(BugRelayEmailNotificationAction.class)
public class BugServiceImpl extends DefaultService<Integer, BugWithBLOBs, BugSearchCriteria> implements BugService {
    static {
        ClassInfoMap.put(BugServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG));
    }

    @Autowired private BugMapper bugMapper;

    @Autowired private BugMapperExt bugMapperExt;

    @Autowired private AsyncEventBus asyncEventBus;

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

                CacheUtils.cleanCaches(record.getSaccountid(),
                        ProjectService.class, ProjectGenericTaskService.class,
                        ProjectMemberService.class,
                        ProjectActivityStreamService.class);

                return super.saveWithSession(record, username);
            } else {
                throw new MyCollabException("Timeout operation");
            }
        } catch (InterruptedException e) {
            throw new MyCollabException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Integer updateWithSession(BugWithBLOBs record, String username) {
        CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
                ProjectActivityStreamService.class,
                ItemTimeLoggingService.class);
        return super.updateWithSession(record, username);
    }

    @Override
    public Integer updateSelectiveWithSession(BugWithBLOBs record, String username) {
        CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class,
                ProjectActivityStreamService.class,
                ItemTimeLoggingService.class);
        return super.updateSelectiveWithSession(record, username);
    }

    @Override
    public Integer removeWithSession(Integer primaryKey, String username,
                                     Integer accountId) {
        CacheUtils.cleanCaches(accountId, ProjectService.class,
                ProjectGenericTaskService.class, ProjectMemberService.class,
                ProjectActivityStreamService.class,
                ItemTimeLoggingService.class);
        SimpleBug bug = findById(primaryKey, accountId);
        DeleteProjectBugEvent event = new DeleteProjectBugEvent(username, accountId,
                bug.getProjectid(), primaryKey);
        asyncEventBus.post(event);
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
    public SimpleBug findById(Integer bugId, Integer sAccountId) {
        return bugMapperExt.getBugById(bugId);
    }

    @Override
    public List<BugStatusGroupItem> getBugStatusGroupItemBaseComponent(
            @CacheKey BugSearchCriteria criteria) {
        return bugMapperExt.getBugStatusGroupItemBaseComponent(criteria);
    }

    @Override
    public SimpleBug findByProjectAndBugKey(Integer bugKey,
                                            String projectShortName, Integer sAccountId) {
        return bugMapperExt.findByProjectAndBugKey(bugKey, projectShortName,
                sAccountId);
    }
}
