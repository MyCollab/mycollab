/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.arguments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class SearchCriteria implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ASC = "ASC";

	public static final String DESC = "DESC";

	private String orderByField;

	private String sortDirection;

	private NumberSearchField saccountid;

	private List<SearchField> extraFields;

	public SearchCriteria() {
		saccountid = new NumberSearchField(GroupIdProvider.getAccountId());
	}

	public String getOrderByField() {
		return orderByField;
	}

	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public NumberSearchField getSaccountid() {
		return saccountid;
	}

	public void setSaccountid(NumberSearchField saccountid) {
		this.saccountid = saccountid;
	}

	public List<SearchField> getExtraFields() {
		return extraFields;
	}

	public void setExtraFields(List<SearchField> extraFields) {
		this.extraFields = extraFields;
	}

	public SearchCriteria addExtraField(SearchField extraField) {
		if (extraFields == null) {
			extraFields = new ArrayList<>();
		}
		extraFields.add(extraField);
		return this;
	}
}
