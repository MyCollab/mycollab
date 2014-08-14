package com.esofthead.mycollab.module.wiki.domain;

import com.esofthead.mycollab.core.arguments.NotBindable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class Page extends WikiResource {

	private String subject;

	private String content;

	@NotBindable
	private boolean isLock;

	private String category;

	private String status;
	
	public Page() {
		super();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isLock() {
		return isLock;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		int index = this.path.lastIndexOf("/");
		return (index < 0) ? this.path : this.path.substring(index + 1);
	}
}
