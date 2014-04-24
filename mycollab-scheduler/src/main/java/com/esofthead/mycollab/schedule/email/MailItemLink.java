package com.esofthead.mycollab.schedule.email;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
@Deprecated
public class MailItemLink implements Serializable {
	private static final long serialVersionUID = 1L;

	private String link;
	private String displayname;

	public MailItemLink(String link, String displayname) {
		this.link = link;
		this.displayname = displayname;
	}

	public String getWebLink() {
		return link;
	}

	public void setWebLink(String link) {
		this.link = link;
	}

	public String getDisplayName() {
		return displayname;
	}

	public void setDisplayName(String displayname) {
		this.displayname = displayname;
	}
}
