/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.esofthead.mycollab.vaadin.ui;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
@SuppressWarnings("serial")
public class DateSelectionComboBox extends ValueComboBox {

	public static final String EQUAL = "Equals";
	public static final String NOTON = "Not On";
	public static final String AFTER = "After";
	public static final String BEFORE = "Before";
	public static final String LAST7DAYS = "Last 7 Days";
	public static final String NEXT7DAYS = "Next 7 Days";
	public static final String LAST30DAYS = "Last 30 Days";
	public static final String NEXT30DAYS = "Next 30 Days";
	public static final String LASTMONTH = "Last Month";
	public static final String THISMONTH = "This Month";
	public static final String NEXTMONTH = "Next Month";
	public static final String LASTYEAR = "Last Year";
	public static final String THISYEAR = "This Year";
	public static final String NEXTYEAR = "Next Year";
	public static final String ISBETWEEN = "Is Between";

	public DateSelectionComboBox() {
		super();
		this.loadData(EQUAL, NOTON, AFTER, BEFORE, LAST7DAYS, NEXT7DAYS,
				LAST30DAYS, NEXT30DAYS, LASTMONTH, THISMONTH, NEXTMONTH,
				LASTYEAR, THISYEAR, NEXTYEAR, ISBETWEEN);
	}
}
