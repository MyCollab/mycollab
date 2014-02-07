package com.esofthead.mycollab.module.mail;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class MailTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	private String path;

	private String subject;

	private String body;
	
	private Date createdDate;
	
	private Date lastUpdatedDate;
	
	private String module;
	
	private String createdUser;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
}
