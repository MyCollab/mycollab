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

import java.util.Locale;

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.configuration.LocaleHelper;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class MailContext<B> {
	private SimpleRelayEmailNotification emailNotification;
	private SimpleUser user;
	private B wrappedBean;
	private String siteUrl;
	private Locale locale;

	public MailContext(SimpleRelayEmailNotification emailNotification,
			SimpleUser user, String siteUrl) {
		this.emailNotification = emailNotification;
		this.user = user;
		this.locale = LocaleHelper.toLocale(user.getLanguage());
		this.siteUrl = siteUrl;
	}

	public SimpleUser getUser() {
		return user;
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

	public Locale getLocale() {
		return locale;
	}

	public SimpleRelayEmailNotification getEmailNotification() {
		return emailNotification;
	}

	public String getMessage(Enum<?> key, Object... params) {
		return LocalizationHelper.getMessage(locale, key, params);
	}

	public int getSaccountid() {
		return emailNotification.getSaccountid();
	}

	public String getChangeByUserFullName() {
		return emailNotification.getChangeByUserFullName();
	}

	public Integer getTypeid() {
		return emailNotification.getTypeid();
	}

	public String getType() {
		return emailNotification.getType();
	}

	public String templatePath(String resourcePath) {
		return MailUtils.templatePath(resourcePath, locale);
	}
}
