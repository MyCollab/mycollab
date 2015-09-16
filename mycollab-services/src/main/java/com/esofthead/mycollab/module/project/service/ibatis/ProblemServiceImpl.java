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

import com.esofthead.mycollab.cache.CleanCacheEvent;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.ProblemMapper;
import com.esofthead.mycollab.module.project.dao.ProblemMapperExt;
import com.esofthead.mycollab.module.project.domain.Problem;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.criteria.ProblemSearchCriteria;
import com.esofthead.mycollab.module.project.esb.DeleteProjectProblemEvent;
import com.esofthead.mycollab.module.project.service.*;
import com.esofthead.mycollab.schedule.email.project.ProjectProblemRelayEmailNotificationAction;
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
@Traceable(nameField = "issuename", extraFieldName = "projectid", notifyAgent = ProjectProblemRelayEmailNotificationAction.class)
@Watchable(userFieldName = "assigntouser", extraTypeId = "projectid")
public class ProblemServiceImpl extends DefaultService<Integer, Problem, ProblemSearchCriteria> implements ProblemService {

    static {
        ClassInfoMap.put(ProblemServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.PROBLEM));
    }

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemMapperExt problemMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, Problem> getCrudMapper() {
        return problemMapper;
    }

    @Override
    public ISearchableDAO<ProblemSearchCriteria> getSearchMapper() {
        return problemMapperExt;
    }

    @Override
    public SimpleProblem findById(Integer problemId, Integer sAccountId) {
        return problemMapperExt.findProblemById(problemId);
    }

    @Override
    public Integer saveWithSession(Problem record, String username) {
        Integer recordId = super.saveWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class}));
        return recordId;
    }

    @Override
    public Integer updateWithSession(Problem record, String username) {
        int result = super.updateWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class}));
        return result;
    }

    @Override
    public void removeByCriteria(ProblemSearchCriteria criteria, Integer accountId) {
        super.removeByCriteria(criteria, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class, ItemTimeLoggingService.class}));
    }

    @Override
    public void massRemoveWithSession(List<Problem> problems, String username, Integer accountId) {
        super.massRemoveWithSession(problems, username, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class, ItemTimeLoggingService.class}));
        DeleteProjectProblemEvent event = new DeleteProjectProblemEvent(problems.toArray(new Problem[problems.size()]),
                username, accountId);
        asyncEventBus.post(event);
    }

    @Override
    public void massUpdateWithSession(Problem record, List<Integer> primaryKeys, Integer accountId) {
        super.massUpdateWithSession(record, primaryKeys, accountId);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectGenericTaskService.class,
                ProjectActivityStreamService.class}));
    }
}
