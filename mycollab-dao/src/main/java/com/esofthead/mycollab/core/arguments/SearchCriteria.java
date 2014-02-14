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
/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
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

	private static String NULL_EXPR = "%s.%s is null";

	private static String NOT_NULL_EXPR = "%s.%s is not null";

	private static String EQUAL_EXPR = "%s.%s = ";

	private static String NOT_EQUAL_EXPR = "%s.%s <> ";

	private static String LIKE_EXPR = "%s.%s like ";

	private static String NOT_LIKE_EXPR = "%s.%s not like ";

	private static String IN_EXPR = "%s.%s in ";

	private static String NOT_IN_EXPR = "%s.%s not in ";

	public static String ASC = "ASC";

	public static String DESC = "DESC";

	protected String orderByField;

	protected String sortDirection;

	protected NumberSearchField saccountid;

	protected List<ExtSearchField> extraFields;

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

	public List<ExtSearchField> getExtraFields() {
		return extraFields;
	}

	public void setExtraFields(List<ExtSearchField> extraFields) {
		this.extraFields = extraFields;
	}

	public SearchCriteria addExtraField(ExtSearchField extraField) {
		if (extraFields == null) {
			extraFields = new ArrayList<ExtSearchField>();
		}
		extraFields.add(extraField);
		return this;
	}

	public SearchCriteria andFieldIsNull(String oper, String table, String field) {
		return addExtraField(new NoValueSearchField(oper, String.format(
				NULL_EXPR, table, field)));
	}

	public SearchCriteria andFieldIsNotNull(String oper, String table,
			String field) {
		return addExtraField(new NoValueSearchField(oper, String.format(
				NOT_NULL_EXPR, table, field)));
	}

	public SearchCriteria andFieldIsEqual(String oper, String table,
			String field, String value) {
		return addExtraField(new OneValueSearchField(oper, String.format(
				EQUAL_EXPR, table, field), value));
	}

	public SearchCriteria andFieldIsNotEqual(String oper, String table,
			String field, String value) {
		return addExtraField(new OneValueSearchField(oper, String.format(
				NOT_EQUAL_EXPR, table, field), value));
	}

	public SearchCriteria andFieldIsLike(String oper, String table,
			String field, String value) {
		return addExtraField(new OneValueSearchField(oper, String.format(
				LIKE_EXPR, table, field), value));
	}

	public SearchCriteria andFieldIsNotLike(String oper, String table,
			String field, String value) {
		return addExtraField(new OneValueSearchField(oper, String.format(
				NOT_LIKE_EXPR, table, field), value));
	}

	public SearchCriteria andFieldInList(String oper, String table,
			String field, List<?> value) {
		return addExtraField(new ListValueSearchField(oper, String.format(
				IN_EXPR, table, field), value));
	}

	public SearchCriteria andFieldNotInList(String oper, String table,
			String field, List<?> value) {
		return addExtraField(new ListValueSearchField(oper, String.format(
				NOT_IN_EXPR, table, field), value));
	}
}
