/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	private String siteUrl;

	public MailContext(B wrappedBean, SimpleUser user, String siteUrl) {
		this.wrappedBean = wrappedBean;
		this.user = user;
		this.siteUrl = siteUrl;
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

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
}
