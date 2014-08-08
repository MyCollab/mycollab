package com.esofthead.mycollab.module.wiki.domain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class Page extends WikiResource {
	public static final String PUBLIC = "public";

	public static final String PRIVATE = "private";

	public static final String ARCHIEVED = "archieved";

	private String subject;

	private String content;

	private boolean isLock;

	private String category;

	private String status;

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
}
