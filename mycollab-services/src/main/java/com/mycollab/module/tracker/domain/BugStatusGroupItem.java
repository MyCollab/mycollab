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
package com.mycollab.module.tracker.domain;

public class BugStatusGroupItem {

	private String status;
	
	private String groupname;

	private Integer numOpen;

	private Integer numInProgress;

	private Integer numVerified;

	private Integer numResolved;

	private Integer numReOpenned;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public Integer getNumOpen() {
		return numOpen;
	}

	public void setNumOpen(Integer numOpen) {
		this.numOpen = numOpen;
	}

	public Integer getNumInProgress() {
		return numInProgress;
	}

	public void setNumInProgress(Integer numInProgress) {
		this.numInProgress = numInProgress;
	}

	public Integer getNumVerified() {
		return numVerified;
	}

	public void setNumVerified(Integer numVerified) {
		this.numVerified = numVerified;
	}

	public Integer getNumResolved() {
		return numResolved;
	}

	public void setNumResolved(Integer numResolved) {
		this.numResolved = numResolved;
	}

	public Integer getNumReOpenned() {
		return numReOpenned;
	}

	public void setNumReOpenned(Integer numReOpenned) {
		this.numReOpenned = numReOpenned;
	}
}
