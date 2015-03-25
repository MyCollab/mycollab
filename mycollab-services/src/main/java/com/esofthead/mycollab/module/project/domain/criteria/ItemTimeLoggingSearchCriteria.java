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
package com.esofthead.mycollab.module.project.domain.criteria;

import java.util.Calendar;
import java.util.Date;

import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ItemTimeLoggingSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	private SetSearchField<Integer> projectIds;

	private SetSearchField<String> logUsers;

	private RangeDateSearchField rangeDate;

	private StringSearchField type;

	private NumberSearchField typeId;

	private BooleanSearchField isBillable;

	public static RangeDateSearchField getCurrentRangeDateOfWeekSearchField() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Date fDate = c.getTime();
		Date tDate = DateTimeUtils.subtractOrAddDayDuration(fDate, 7);
		return new RangeDateSearchField(fDate, tDate);
	}

	public SetSearchField<Integer> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(SetSearchField<Integer> projectIds) {
		this.projectIds = projectIds;
	}

	public SetSearchField<String> getLogUsers() {
		return logUsers;
	}

	public void setLogUsers(SetSearchField<String> logUsers) {
		this.logUsers = logUsers;
	}

	public RangeDateSearchField getRangeDate() {
		return rangeDate;
	}

	public void setRangeDate(RangeDateSearchField rangeDate) {
		this.rangeDate = rangeDate;
	}

	public void setType(StringSearchField type) {
		this.type = type;
	}

	public StringSearchField getType() {
		return type;
	}

	public void setTypeId(NumberSearchField typeId) {
		this.typeId = typeId;
	}

	public NumberSearchField getTypeId() {
		return typeId;
	}

	public BooleanSearchField getIsBillable() {
		return isBillable;
	}

	public void setIsBillable(BooleanSearchField isBillable) {
		this.isBillable = isBillable;
	}
}
