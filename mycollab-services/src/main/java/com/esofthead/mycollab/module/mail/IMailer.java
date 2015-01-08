/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.mail;

import java.util.List;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.module.user.domain.SimpleUser;

/**
 * Abstract mailer. Note: in MyCollab we now support send HTML content email
 * only.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface IMailer {
	/**
	 * 
	 * @param fromEmail
	 * @param fromName
	 * @param toEmail
	 * @param ccEmail
	 * @param bccEmail
	 * @param subject
	 * @param html
	 */
	void sendHTMLMail(String fromEmail, String fromName,
			List<MailRecipientField> toEmail, List<MailRecipientField> ccEmail,
			List<MailRecipientField> bccEmail, String subject, String html);

	/**
	 * 
	 * @param fromEmail
	 * @param fromName
	 * @param toEmail
	 * @param ccEmail
	 * @param bccEmail
	 * @param subject
	 * @param html
	 * @param attachments
	 */
	void sendHTMLMail(String fromEmail, String fromName,
			List<MailRecipientField> toEmail, List<MailRecipientField> ccEmail,
			List<MailRecipientField> bccEmail, String subject, String html,
			List<EmailAttachementSource> attachments);

	/**
	 * 
	 * @param fromEmail
	 * @param fromName
	 * @param users
	 * @param subject
	 * @param html
	 * @param attachment
	 */
	void sendHTMLMail(String fromEmail, String fromName,
			List<SimpleUser> users, String subject, String html,
			List<EmailAttachementSource> attachment);
}
