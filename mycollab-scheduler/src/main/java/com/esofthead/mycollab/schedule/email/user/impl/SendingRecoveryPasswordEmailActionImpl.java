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
package com.esofthead.mycollab.schedule.email.user.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.domain.RelayEmailWithBLOBs;
import com.esofthead.mycollab.configuration.LocaleHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.mail.IContentGenerator;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.user.SendingRecoveryPasswordEmailAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class SendingRecoveryPasswordEmailActionImpl implements
		SendingRecoveryPasswordEmailAction {

	@Autowired
	private ExtMailService extMailService;

	@Autowired
	private UserService userService;

	@Autowired
	private IContentGenerator contentGenerator;

	@Override
	public void sendEmail(RelayEmailWithBLOBs relayEmail) {
		String username = relayEmail.getRecipients();
		if (username != null) {
			User user = userService.findUserByUserName(username);
			String subdomain = "api";
			String recoveryPasswordURL = SiteConfiguration
					.getSiteUrl(subdomain)
					+ "user/recoverypassword/"
					+ UrlEncodeDecoder.encode(username);
			Locale locale = LocaleHelper.toLocale(user.getLanguage());

			contentGenerator.putVariable("username", user.getUsername());
			contentGenerator.putVariable("urlRecoveryPassword",
					recoveryPasswordURL);

			MailRecipientField recipient = new MailRecipientField(
					user.getEmail(), user.getUsername());
			List<MailRecipientField> lst = new ArrayList<MailRecipientField>();
			lst.add(recipient);
			extMailService
					.sendHTMLMail(
							"noreply@mycollab.com",
							SiteConfiguration.getSiteName(),
							lst,
							null,
							null,
							contentGenerator
									.generateSubjectContent("[MyCollab] - User recovery password"),
							contentGenerator.generateBodyContent(MailUtils
									.templatePath(
											"templates/email/user/userRecoveryPasswordNotifier.mt",
											locale)), null);
		}
	}
}
