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

import com.google.common.eventbus.AsyncEventBus;
import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.CleanCache;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.dao.MilestoneMapper;
import com.mycollab.module.project.dao.MilestoneMapperExt;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    private MilestoneMapper milestoneMapper;

    @Autowired
    private MilestoneMapperExt milestoneMapperExt;

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
            record.setStatus(MilestoneStatus.InProgress.name());
        }
        return super.saveWithSession(record, username);
    }

    @CleanCache
    public void postDirtyUpdate(Integer sAccountId) {
        asyncEventBus.post(new CleanCacheEvent(sAccountId, new Class[]{ProjectService.class,
                GanttAssignmentService.class, ProjectTicketService.class, ProjectActivityStreamService.class}));
    }

    @Override
    public void massUpdateOptionIndexes(List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate("UPDATE `m_prj_milestone` SET `orderIndex`=? WHERE `id`=?", new
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
