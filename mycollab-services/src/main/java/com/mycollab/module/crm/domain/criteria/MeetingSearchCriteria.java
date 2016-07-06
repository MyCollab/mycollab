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

package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.BitSearchField;
import com.mycollab.db.arguments.DateTimeSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MeetingSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	private SetSearchField<String> assignUsers;
	private NumberSearchField id;
	private BitSearchField isClosed;
	private DateTimeSearchField startDate;
	private DateTimeSearchField endDate;

	public SetSearchField<String> getAssignUsers() {
		return assignUsers;
	}

	public void setAssignUsers(SetSearchField<String> assignUsers) {
		this.assignUsers = assignUsers;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public NumberSearchField getId() {
		return id;
	}

	public void setIsClosed(BitSearchField isClosed) {
		this.isClosed = isClosed;
	}

	public BitSearchField getIsClosed() {
		return isClosed;
	}

	public DateTimeSearchField getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTimeSearchField startDate) {
		this.startDate = startDate;
	}

	public DateTimeSearchField getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTimeSearchField endDate) {
		this.endDate = endDate;
	}
}
