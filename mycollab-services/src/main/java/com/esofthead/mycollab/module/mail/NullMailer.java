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

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Dummy a mailer in case email configuration not properly set.
 *
 * @author MyCollab Ltd
 * @since 5.0.9
 */
public class NullMailer implements IMailer {
    private static final Logger LOG = LoggerFactory.getLogger(NullMailer.class);

    @Override
    public void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmail, List<MailRecipientField> ccEmail,
                             List<MailRecipientField> bccEmail, String subject, String html) {
        LOG.info("You did not configure email. So Email feature is disable and MyCollab can not send any notification via email.");
    }

    @Override
    public void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmail, List<MailRecipientField> ccEmail,
                             List<MailRecipientField> bccEmail, String subject, String html, List<EmailAttachementSource> attachments) {
        LOG.info("You did not configure email. So Email feature is disable and MyCollab can not send any notification via email.");

    }

    @Override
    public void sendHTMLMail(String fromEmail, String fromName, List<SimpleUser> users, String subject, String html,
                             List<EmailAttachementSource> attachment) {
        LOG.info("You did not configure email. So Email feature is disable and MyCollab can not send any notification via email.");

    }

}
