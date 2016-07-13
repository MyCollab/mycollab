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
package com.mycollab.common.service.ibatis;

import com.mycollab.common.dao.OptionValMapper;
import com.mycollab.common.dao.TimelineTrackingCachingMapper;
import com.mycollab.common.dao.TimelineTrackingMapper;
import com.mycollab.common.domain.*;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.common.service.OptionValService;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import com.mycollab.module.project.ProjectTypeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
@Service
public class OptionValServiceImpl extends DefaultCrudService<Integer, OptionVal> implements OptionValService {
    @Autowired
    private OptionValMapper optionValMapper;

    @Autowired
    private TimelineTrackingMapper timelineTrackingMapper;

    @Autowired
    private TimelineTrackingCachingMapper timelineTrackingCachingMapper;

    @Autowired
    private DataSource dataSource;

    @Override
    public ICrudGenericDAO<Integer, OptionVal> getCrudMapper() {
        return optionValMapper;
    }

    @Override
    public List<OptionVal> findOptionVals(String type, Integer projectId, Integer sAccountId) {
        OptionValExample ex = new OptionValExample();
        ex.createCriteria().andTypeEqualTo(type).andSaccountidEqualTo(sAccountId).andExtraidEqualTo(projectId);
        ex.setOrderByClause("orderIndex ASC");
        ex.setDistinct(true);

        return optionValMapper.selectByExampleWithBLOBs(ex);
    }

    @Override
    public List<OptionVal> findOptionValsExcludeClosed(String type, Integer projectId, @CacheKey Integer sAccountId) {
        OptionValExample ex = new OptionValExample();
        ex.createCriteria().andTypeEqualTo(type).andTypevalNotEqualTo(OptionI18nEnum.StatusI18nEnum.Closed.name())
                .andSaccountidEqualTo(sAccountId).andExtraidEqualTo(projectId);
        ex.setOrderByClause("orderIndex ASC");
        ex.setDistinct(true);

        return optionValMapper.selectByExampleWithBLOBs(ex);
    }

    @Override
    public Integer saveWithSession(OptionVal record, String username) {
        checkSaveOrUpdateValid(record);
        return super.saveWithSession(record, username);
    }

    private void checkSaveOrUpdateValid(OptionVal record) {
        String typeVal = record.getTypeval();
        if (Boolean.TRUE.equals(record.getIsdefault())) {
            OptionValExample ex = new OptionValExample();
            ex.createCriteria().andTypeEqualTo(record.getType()).andTypevalEqualTo(typeVal)
                    .andFieldgroupEqualTo(record.getFieldgroup())
                    .andSaccountidEqualTo(record.getSaccountid());
            if (optionValMapper.countByExample(ex) > 0) {
                throw new UserInvalidInputException("There is already column name " + typeVal);
            }
        } else {
            OptionValExample ex = new OptionValExample();
            ex.createCriteria().andTypeEqualTo(record.getType()).andTypevalEqualTo(typeVal)
                    .andFieldgroupEqualTo(record.getFieldgroup()).andSaccountidEqualTo(record
                    .getSaccountid()).andIsdefaultEqualTo(Boolean.FALSE);
            if (optionValMapper.countByExample(ex) > 0) {
                throw new UserInvalidInputException("There is already column name " + typeVal);
            }
        }
    }

    @Override
    public Integer updateWithSession(OptionVal record, String username) {
        if (Boolean.FALSE.equals(record.getIsdefault())) {
            TimelineTrackingExample timelineTrackingExample = new TimelineTrackingExample();
            timelineTrackingExample.createCriteria().andTypeEqualTo(record.getType()).andFieldvalEqualTo(record.getTypeval())
                    .andFieldgroupEqualTo(record.getFieldgroup()).andExtratypeidEqualTo(record.getExtraid());
            TimelineTracking timelineTracking = new TimelineTracking();
            timelineTracking.setFieldval(record.getTypeval());
            timelineTrackingMapper.updateByExampleSelective(timelineTracking, timelineTrackingExample);

            TimelineTrackingCachingExample timelineTrackingCachingExample = new TimelineTrackingCachingExample();
            timelineTrackingCachingExample.createCriteria().andTypeEqualTo(record.getType()).andFieldvalEqualTo
                    (record.getTypeval()).andFieldgroupEqualTo(record.getFieldgroup()).andExtratypeidEqualTo(record.getExtraid());
            TimelineTrackingCaching timelineTrackingCaching = new TimelineTrackingCaching();
            timelineTrackingCaching.setFieldval(record.getTypeval());
            timelineTrackingCachingMapper.updateByExampleSelective(timelineTrackingCaching,
                    timelineTrackingCachingExample);
        }
        return super.updateWithSession(record, username);
    }

    @Override
    public void massUpdateOptionIndexes(final List<Map<String, Integer>> mapIndexes, Integer sAccountId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate("UPDATE `m_options` SET `orderIndex`=? WHERE `id`=?", new
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
    public boolean isExistedOptionVal(String type, String typeVal, String fieldGroup, Integer projectId, Integer sAccountId) {
        OptionValExample ex = new OptionValExample();
        ex.createCriteria().andTypeEqualTo(type).andTypevalEqualTo(typeVal).andFieldgroupEqualTo(fieldGroup)
                .andSaccountidEqualTo(sAccountId).andExtraidEqualTo(projectId);
        return (optionValMapper.countByExample(ex) > 0);
    }

    @Override
    public void createDefaultOptions(Integer sAccountId) {
        OptionVal option = new OptionVal();
        option.setCreatedtime(new GregorianCalendar().getTime());
        option.setIsdefault(true);
        option.setSaccountid(sAccountId);
        option.setType(ProjectTypeConstants.TASK);
        option.setTypeval(OptionI18nEnum.StatusI18nEnum.Open.name());
        option.setColor("fdde86");
        option.setFieldgroup("status");
        saveWithSession(option, null);

        option.setTypeval(OptionI18nEnum.StatusI18nEnum.InProgress.name());
        option.setId(null);
        saveWithSession(option, null);

        option.setTypeval(OptionI18nEnum.StatusI18nEnum.Archived.name());
        option.setId(null);
        saveWithSession(option, null);

        option.setTypeval(OptionI18nEnum.StatusI18nEnum.Closed.name());
        option.setId(null);
        saveWithSession(option, null);

        option.setTypeval(OptionI18nEnum.StatusI18nEnum.Pending.name());
        option.setId(null);
        saveWithSession(option, null);

        option.setType(ProjectTypeConstants.MILESTONE);
        option.setTypeval(com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus.Closed.name());
        option.setId(null);
        saveWithSession(option, null);

        option.setTypeval(com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus.InProgress.name());
        option.setId(null);
        saveWithSession(option, null);

        option.setTypeval(com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus.Future.name());
        option.setId(null);
        saveWithSession(option, null);
    }
}
