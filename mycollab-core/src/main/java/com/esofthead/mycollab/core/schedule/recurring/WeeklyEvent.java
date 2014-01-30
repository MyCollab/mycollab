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

public class WeeklyEvent implements RecurringEvent {
	private int repeatInWeekNum;

	private boolean happenInSunday;

	private boolean happenInMonday;
	private boolean happenInTuesday;
	private boolean happenInWednesday;
	private boolean happenInThursday;
	private boolean happenInFriday;
	private boolean happenInSaturday;

	public int getRepeatInWeekNum() {
		return repeatInWeekNum;
	}

	public void setRepeatInWeekNum(int repeatInWeekNum) {
		this.repeatInWeekNum = repeatInWeekNum;
	}

	public boolean isHappenInSunday() {
		return happenInSunday;
	}

	public void setHappenInSunday(boolean happenInSunday) {
		this.happenInSunday = happenInSunday;
	}

	public boolean isHappenInMonday() {
		return happenInMonday;
	}

	public void setHappenInMonday(boolean happenInMonday) {
		this.happenInMonday = happenInMonday;
	}

	public boolean isHappenInTuesday() {
		return happenInTuesday;
	}

	public void setHappenInTuesday(boolean happenInTuesday) {
		this.happenInTuesday = happenInTuesday;
	}

	public boolean isHappenInWednesday() {
		return happenInWednesday;
	}

	public void setHappenInWednesday(boolean happenInWednesday) {
		this.happenInWednesday = happenInWednesday;
	}

	public boolean isHappenInThursday() {
		return happenInThursday;
	}

	public void setHappenInThursday(boolean happenInThursday) {
		this.happenInThursday = happenInThursday;
	}

	public boolean isHappenInFriday() {
		return happenInFriday;
	}

	public void setHappenInFriday(boolean happenInFriday) {
		this.happenInFriday = happenInFriday;
	}

	public boolean isHappenInSaturday() {
		return happenInSaturday;
	}

	public void setHappenInSaturday(boolean happenInSaturday) {
		this.happenInSaturday = happenInSaturday;
	}

}
