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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.dao.ItemTimeLoggingMapper;
import com.esofthead.mycollab.module.project.dao.ItemTimeLoggingMapperExt;
import com.esofthead.mycollab.module.project.domain.ItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class ItemTimeLoggingServiceImpl extends
		DefaultService<Integer, ItemTimeLogging, ItemTimeLoggingSearchCriteria>
		implements ItemTimeLoggingService {

	@Autowired
	private ItemTimeLoggingMapper itemTimeLoggingMapper;

	@Autowired
	private ItemTimeLoggingMapperExt itemTimeLoggingMapperExt;

	@Override
	public ICrudGenericDAO getCrudMapper() {
		return itemTimeLoggingMapper;
	}

	@Override
	public ISearchableDAO<ItemTimeLoggingSearchCriteria> getSearchMapper() {
		return itemTimeLoggingMapperExt;
	}

	@Override
	public int saveWithSession(ItemTimeLogging record, String username) {
		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class);
		return super.saveWithSession(record, username);
	}

	@Override
	public int updateWithSession(ItemTimeLogging record, String username) {
		CacheUtils.cleanCaches(record.getSaccountid(), ProjectService.class);
		return super.updateWithSession(record, username);
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int accountId) {
		CacheUtils.cleanCaches(accountId, ProjectService.class);
		return super.removeWithSession(primaryKey, username, accountId);
	}

	@Override
	public Double getTotalHoursByCriteria(ItemTimeLoggingSearchCriteria criteria) {
		Double value = itemTimeLoggingMapperExt
				.getTotalHoursByCriteria(criteria);
		return (value != null) ? value : 0;
	}

	@Override
	public void batchSaveTimeLogging(final List<ItemTimeLogging> timeLoggings,
			@CacheKey int sAccountId) {
		DataSource dataSource = ApplicationContextUtil
				.getSpringBean(DataSource.class);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate
				.batchUpdate(
						"insert into m_prj_time_logging (projectId, type, typeid, logValue, loguser, createdTime, lastUpdatedTime, sAccountId, logForDay, isBillable, createdUser, "
								+ "note) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new BatchPreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								ItemTimeLogging itemLogging = timeLoggings
										.get(i);
								ps.setInt(1, itemLogging.getProjectid());
								ps.setString(2, itemLogging.getType());

								if (itemLogging.getTypeid() == null) {
									ps.setNull(3, Types.INTEGER);
								} else {
									ps.setInt(3, itemLogging.getTypeid());
								}

								ps.setDouble(4, itemLogging.getLogvalue());
								ps.setString(5, itemLogging.getLoguser());
								ps.setTimestamp(6, new Timestamp(
										new GregorianCalendar().getTime()
												.getTime()));
								ps.setTimestamp(7, new Timestamp(
										new GregorianCalendar().getTime()
												.getTime()));
								ps.setInt(8, itemLogging.getSaccountid());
								ps.setTimestamp(9, new Timestamp(itemLogging
										.getLogforday().getTime()));
								ps.setBoolean(10, itemLogging.getIsbillable());
								ps.setString(11, itemLogging.getCreateduser());
								ps.setString(12, itemLogging.getNote());
							}

							@Override
							public int getBatchSize() {
								return timeLoggings.size();
							}
						});
		CacheUtils.cleanCaches(sAccountId, ItemTimeLoggingService.class);
	}

}
