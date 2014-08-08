package com.esofthead.mycollab.module.wiki.domain;

import java.util.Date;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class WikiResource {
	private Date createdTime;

	private String createdUser;

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
}
