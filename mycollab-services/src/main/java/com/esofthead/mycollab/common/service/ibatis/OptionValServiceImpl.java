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
package com.esofthead.mycollab.common.service.ibatis;

import com.esofthead.mycollab.common.dao.OptionValMapper;
import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.domain.OptionValExample;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.common.service.OptionValService;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        String typeVal = record.getTypeval();
        if (Boolean.TRUE.equals(record.getIsdefault())) {
            OptionValExample ex = new OptionValExample();
            ex.createCriteria().andTypevalEqualTo(typeVal).andSaccountidEqualTo(record.getSaccountid());
            if (optionValMapper.countByExample(ex) > 0) {
                throw new UserInvalidInputException("There is already column name " + typeVal);
            }
        } else {
            OptionValExample ex = new OptionValExample();
            ex.createCriteria().andTypevalEqualTo(typeVal).andSaccountidEqualTo(record.getSaccountid())
                    .andIsdefaultEqualTo(Boolean.FALSE);
            if (optionValMapper.countByExample(ex) > 0) {
                throw new UserInvalidInputException("There is already column name " + typeVal);
            }
        }

        return super.saveWithSession(record, username);
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
    public boolean isExistedOptionVal(String type, String typeVal, Integer projectId, Integer sAccountId) {
        OptionValExample ex = new OptionValExample();
        ex.createCriteria().andTypeEqualTo(type).andTypevalEqualTo(typeVal).andSaccountidEqualTo(sAccountId)
                .andExtraidEqualTo(projectId);
        return (optionValMapper.countByExample(ex) > 0);
    }
}
