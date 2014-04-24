package com.esofthead.mycollab.schedule.email;

import com.esofthead.mycollab.module.user.domain.SimpleUser;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class MailContext<B> {
	private SimpleUser user;
	private B wrappedBean;

	public MailContext(B wrappedBean, SimpleUser user) {
		this.wrappedBean = wrappedBean;
		this.user = user;
	}

	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
	}

	public B getWrappedBean() {
		return wrappedBean;
	}

	public void setWrappedBean(B wrappedBean) {
		this.wrappedBean = wrappedBean;
	}
}
