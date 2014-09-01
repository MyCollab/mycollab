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

import java.util.Date;

import com.esofthead.mycollab.core.utils.DateTimeUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class DateSearchField extends SearchField {
	private static final long serialVersionUID = 1L;
	
	public static String LESSTHAN = "<";
	public static String LESSTHANEQUAL = "<=";
	public static String GREATERTHAN = ">";
	public static String GREATERTHANEQUAL = ">=";
	public static String EQUAL = "=";
	public static String NOTEQUAL = "<>";

	private Date value;
	private String comparision;

	public DateSearchField() {
		this(AND, null, null);
	}

	public DateSearchField(String oper, Date value) {
		this(oper, DateTimeSearchField.LESSTHAN, value);
	}

	public DateSearchField(String oper, String comparision, Date dateVal) {
		this.operation = oper;
		this.comparision = comparision;
		this.value = DateTimeUtils.trimHMSOfDate(DateTimeUtils
				.convertTimeFromSystemTimezoneToUTC(dateVal.getTime()));
	}

	public Date getValue() {
		return value;
	}

	public void setValue(Date value) {
		this.value = value;
	}

	public String getComparision() {
		return comparision;
	}

	public void setComparision(String comparision) {
		this.comparision = comparision;
	}
}
