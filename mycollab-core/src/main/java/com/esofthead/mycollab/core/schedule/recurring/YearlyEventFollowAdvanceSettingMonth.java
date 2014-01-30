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
package com.esofthead.mycollab.core.schedule.recurring;

public class YearlyEventFollowAdvanceSettingMonth implements RecurringEvent {
	private int happenIn;
	private String kindOfDay;
	private String month;

	public int getHappenIn() {
		return happenIn;
	}

	public void setHappenIn(int happenIn) {
		this.happenIn = happenIn;
	}

	public String getKindOfDay() {
		return kindOfDay;
	}

	public void setKindOfDay(String kindOfDay) {
		this.kindOfDay = kindOfDay;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

}
