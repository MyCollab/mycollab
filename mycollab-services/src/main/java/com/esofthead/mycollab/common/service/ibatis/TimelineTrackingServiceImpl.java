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

import com.esofthead.mycollab.common.dao.TimelineTrackingCachingMapperExt;
import com.esofthead.mycollab.common.dao.TimelineTrackingMapper;
import com.esofthead.mycollab.common.dao.TimelineTrackingMapperExt;
import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.domain.TimelineTracking;
import com.esofthead.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.esofthead.mycollab.common.service.TimelineTrackingService;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
@Service
public class TimelineTrackingServiceImpl extends DefaultCrudService<Integer, TimelineTracking> implements TimelineTrackingService {

    private static final Logger LOG = LoggerFactory.getLogger(TimelineTrackingServiceImpl.class);

    @Autowired
    private TimelineTrackingMapper timelineTrackingMapper;

    @Autowired
    private TimelineTrackingMapperExt timelineTrackingMapperExt;

    @Autowired
    private TimelineTrackingCachingMapperExt timelineTrackingCachingMapperExt;

    @Autowired
    private DataSource dataSource;

    @Override
    public ICrudGenericDAO<Integer, TimelineTracking> getCrudMapper() {
        return timelineTrackingMapper;
    }

    @Override
    public Map<String, List<GroupItem>> findTimelineItems(List<String> groupVals, Date start, Date end,
                                                          TimelineTrackingSearchCriteria criteria) {
        try {
            DateTime startDate = new DateTime(start);
            final DateTime endDate = new DateTime(end);
            if (startDate.isAfter(endDate)) {
                throw new UserInvalidInputException("Start date must be greater than end date");
            }
            List<Date> dates = boundDays(startDate, endDate.minusDays(1));

            Map<String, List<GroupItem>> items = new HashMap<>();
            List<GroupItem> cacheTimelineItems = timelineTrackingCachingMapperExt.findTimelineItems(groupVals, dates, criteria);

            DateTime calculatedDate = startDate.toDateTime();
            if (cacheTimelineItems.size() > 0) {
                GroupItem item = cacheTimelineItems.get(cacheTimelineItems.size() - 1);
                String dateValue = item.getGroupname();
                calculatedDate = DateTime.parse(dateValue, DateTimeFormat.forPattern("yyyy-MM-dd"));
            }

            if (cacheTimelineItems.size() > 0) {
                for (GroupItem map : cacheTimelineItems) {
                    String groupVal = map.getGroupid();
                    Object obj = items.get(groupVal);
                    if (obj == null) {
                        List<GroupItem> itemLst = new ArrayList<>();
                        itemLst.add(map);
                        items.put(groupVal, itemLst);
                    } else {
                        List<GroupItem> itemLst = (List<GroupItem>) obj;
                        itemLst.add(map);
                    }
                }
            }

            dates = boundDays(calculatedDate.plusDays(1), endDate);
            if (dates.size() > 0) {
                boolean isValidForBatchSave = true;
                final String type = criteria.getType().getValue();
                SetSearchField<Integer> extraTypeIds = criteria.getExtraTypeIds();
                Integer tmpExtraTypeId = null;
                if (extraTypeIds != null) {
                    if (extraTypeIds.getValues().size() == 1) {
                        tmpExtraTypeId = extraTypeIds.getValues().iterator().next();
                    } else {
                        isValidForBatchSave = false;
                    }
                }
                final Integer extraTypeId = tmpExtraTypeId;

                final List<Map> timelineItems = timelineTrackingMapperExt.findTimelineItems(groupVals, dates, criteria);
                if (isValidForBatchSave) {
                    final Integer sAccountId = (Integer) criteria.getSaccountid().getValue();
                    final String fieldgroup = criteria.getFieldgroup().getValue();
                    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

                    final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                    final List<Map> filterCollections = new ArrayList<>(Collections2.filter(timelineItems, new
                            Predicate<Map>() {
                                @Override
                                public boolean apply(Map input) {
                                    String dateStr = (String) input.get("groupname");
                                    DateTime dt = formatter.parseDateTime(dateStr);
                                    return !dt.equals(endDate);
                                }
                            }));
                    jdbcTemplate.batchUpdate("INSERT INTO `s_timeline_tracking_cache`(type, fieldval,extratypeid,sAccountId," +
                            "forDay, fieldgroup,count) VALUES(?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            Map item = filterCollections.get(i);
                            preparedStatement.setString(1, type);
                            String fieldVal = (String) item.get("groupid");
                            preparedStatement.setString(2, fieldVal);
                            preparedStatement.setInt(3, extraTypeId);
                            preparedStatement.setInt(4, sAccountId);
                            String dateStr = (String) item.get("groupname");
                            DateTime dt = formatter.parseDateTime(dateStr);
                            preparedStatement.setDate(5, new java.sql.Date(dt.toDate().getTime()));
                            preparedStatement.setString(6, fieldgroup);
                            int value = ((BigDecimal) item.get("value")).intValue();
                            preparedStatement.setInt(7, value);
                        }

                        @Override
                        public int getBatchSize() {
                            return filterCollections.size();
                        }
                    });
                }

                for (Map map : timelineItems) {
                    String groupVal = (String) map.get("groupid");
                    GroupItem item = new GroupItem();
                    item.setValue(((BigDecimal) map.get("value")).intValue());
                    item.setGroupid((String) map.get("groupid"));
                    item.setGroupname((String) map.get("groupname"));
                    Object obj = items.get(groupVal);
                    if (obj == null) {
                        List<GroupItem> itemLst = new ArrayList<>();
                        itemLst.add(item);
                        items.put(groupVal, itemLst);
                    } else {
                        List<GroupItem> itemLst = (List<GroupItem>) obj;
                        itemLst.add(item);
                    }
                }
            }

            return items;
        } catch (Exception e) {
            LOG.error("Error", e);
            return null;
        }
    }

    private List<Date> boundDays(DateTime start, DateTime end) {
        Duration duration = new Duration(start, end);
        long days = duration.getStandardDays();
        List<Date> dates = new ArrayList<>();
        //Will try to get from cache values from the end date to (startdate - 1)
        for (int i = 0; i <= days; i++) {
            dates.add(start.plusDays(i).toDate());
        }
        return dates;
    }
}
