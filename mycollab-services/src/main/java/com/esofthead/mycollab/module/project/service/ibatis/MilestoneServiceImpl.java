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
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.MilestoneMapper;
import com.esofthead.mycollab.module.project.dao.MilestoneMapperExt;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "projectid")
public class MilestoneServiceImpl extends DefaultService<Integer, Milestone, MilestoneSearchCriteria> implements MilestoneService {
    static {
        ClassInfoMap.put(MilestoneServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.MILESTONE));
    }

    @Autowired
    protected MilestoneMapper milestoneMapper;

    @Autowired
    protected MilestoneMapperExt milestoneMapperExt;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, Milestone> getCrudMapper() {
        return milestoneMapper;
    }

    @Override
    public ISearchableDAO<MilestoneSearchCriteria> getSearchMapper() {
        return milestoneMapperExt;
    }

    @Override
    public SimpleMilestone findById(Integer milestoneId, Integer sAccountId) {
        return milestoneMapperExt.findById(milestoneId);
    }

    @Override
    public Integer saveWithSession(Milestone record, String username) {
        if (record.getStatus() == null) {
            record.setStatus(OptionI18nEnum.MilestoneStatus.InProgress.name());
        }
        Integer recordId = super.saveWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class,
                GanttAssignmentService.class, ProjectGenericTaskService.class}));
        return recordId;
    }

    @Override
    public Integer updateWithSession(Milestone record, String username) {
        int result = super.updateWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class,
                GanttAssignmentService.class, ProjectGenericTaskService.class}));
        return result;
    }
}
