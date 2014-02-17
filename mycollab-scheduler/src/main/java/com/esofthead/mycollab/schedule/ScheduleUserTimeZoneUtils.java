/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule;

import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.DateTimeUtils;

/**
 * Utility date in schedule email. It main objective is convert system date
 * value to the right time value associate with user timezone
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ScheduleUserTimeZoneUtils {
	private static Logger log = LoggerFactory
			.getLogger(ScheduleUserTimeZoneUtils.class);

	/**
	 * Format all date value of bean <code>bean</code>, and field array
	 * <code>fieldNames</code> with time zone <code>userTimeZone</code>
	 * 
	 * @param bean
	 *            java bean need to be converted
	 * @param userTimeZone
	 * @param fieldNames
	 *            field name has value is date instance
	 * @return
	 */
	public static <B extends ValuedBean> B formatDateTimeZone(final B bean,
			String userTimeZone, String... fieldNames) {
		for (String fieldName : fieldNames) {
			Date date = null;
			try {
				date = (Date) PropertyUtils.getProperty(bean, fieldName);
				Date dateAssociateTz = DateTimeUtils
						.converToDateWithUserTimeZone(date, userTimeZone);
				if (dateAssociateTz != null) {
					PropertyUtils.setProperty(bean, fieldName, dateAssociateTz);
				}

			} catch (Exception e) {
				log.error(
						"Error while convert time from date {} with timezone {}",
						date, userTimeZone);
			}
		}
		return bean;
	}

	public static SimpleAuditLog formatDate(final SimpleAuditLog auditLog,
			String userTimeZone, String... fieldDateNames) {
		for (AuditChangeItem item : auditLog.getChangeItems()) {
			for (String fieldDateName : fieldDateNames) {
				if (item.getField().equals(fieldDateName)) {
					Date dateNewValue = DateTimeUtils
							.convertDateByFormatW3C(item.getNewvalue());
					Date dateOldValue = DateTimeUtils
							.convertDateByFormatW3C(item.getOldvalue());
					if (dateNewValue == null) {
						item.setNewvalue("");
					} else {
						item.setNewvalue(DateTimeUtils
								.converToStringWithUserTimeZone(dateNewValue,
										userTimeZone));
					}
					if (dateOldValue == null) {
						item.setOldvalue("");
					} else {
						item.setOldvalue(DateTimeUtils
								.converToStringWithUserTimeZone(dateOldValue,
										userTimeZone));
					}
				}
			}
		}
		return auditLog;
	}
}
