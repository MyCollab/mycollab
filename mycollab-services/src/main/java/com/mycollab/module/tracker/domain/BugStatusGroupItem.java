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
