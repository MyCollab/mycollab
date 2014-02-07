package com.esofthead.mycollab.module.mail;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class MailFolder implements Serializable {
	private static final long serialVersionUID = 1L;

	private String path;
	
	private String createdUser;

	private Date createdDate;

	private Date lastUpdatedDate;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
}
