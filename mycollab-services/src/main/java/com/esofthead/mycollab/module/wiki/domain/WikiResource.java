package com.esofthead.mycollab.module.wiki.domain;

import java.util.Calendar;

import com.esofthead.mycollab.core.arguments.NotBindable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class WikiResource {
	@NotBindable
	private Calendar createdTime;

	@NotBindable
	private String createdUser;

	@NotBindable
	protected String path = "";

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
