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
package com.mycollab.module.mail;

import com.mycollab.common.domain.MailRecipientField;
import com.mycollab.configuration.EmailConfiguration;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DefaultMailer implements IMailer {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMailer.class);

    private EmailConfiguration emailConf;

    public DefaultMailer(EmailConfiguration emailConf) {
        this.emailConf = emailConf;
    }

    private HtmlEmail getBasicEmail(String fromEmail, String fromName, List<MailRecipientField> toEmail,
                                    List<MailRecipientField> ccEmail, List<MailRecipientField> bccEmail,
                                    String subject, String html) {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(emailConf.getHost());
            email.setSmtpPort(emailConf.getPort());
            email.setStartTLSEnabled(emailConf.getIsStartTls());
            email.setSSLOnConnect(emailConf.getIsSsl());
            email.setFrom(fromEmail, fromName);
            email.setCharset(EmailConstants.UTF_8);
            for (MailRecipientField aToEmail : toEmail) {
                if (isValidate(aToEmail.getEmail()) && isValidate(aToEmail.getName())) {
                    email.addTo(aToEmail.getEmail(), aToEmail.getName());
                } else {
                    LOG.error(String.format("Invalid to email input: %s---%s", aToEmail.getEmail(), aToEmail.getName()));
                }
            }

            if (CollectionUtils.isNotEmpty(ccEmail)) {
                for (MailRecipientField aCcEmail : ccEmail) {
                    if (isValidate(aCcEmail.getEmail()) && isValidate(aCcEmail.getName())) {
                        email.addCc(aCcEmail.getEmail(), aCcEmail.getName());
                    } else {
                        LOG.error(String.format("Invalid cc email input: %s---%s", aCcEmail.getEmail(), aCcEmail.getName()));
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(bccEmail)) {
                for (MailRecipientField aBccEmail : bccEmail) {
                    if (isValidate(aBccEmail.getEmail()) && isValidate(aBccEmail.getName())) {
                        email.addBcc(aBccEmail.getEmail(), aBccEmail.getName());
                    } else {
                        LOG.error(String.format("Invalid bcc email input: %s---%s", aBccEmail.getEmail(), aBccEmail.getName()));
                    }
                }
            }

            if (emailConf.getUser() != null) {
                email.setAuthentication(emailConf.getUser(), emailConf.getPassword());
            }

            email.setSubject(subject);

            if (StringUtils.isNotBlank(html)) {
                email.setHtmlMsg(html);
            }

            return email;
        } catch (EmailException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmails, List<MailRecipientField> ccEmails,
                             List<MailRecipientField> bccEmails, String subject, String html) {
        try {
            HtmlEmail email = getBasicEmail(fromEmail, fromName, toEmails, ccEmails, bccEmails, subject, html);
            email.send();
        } catch (EmailException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmails,
                             List<MailRecipientField> ccEmails, List<MailRecipientField> bccEmails,
                             String subject, String html, List<? extends AttachmentSource> attachments) {
        try {
            if (CollectionUtils.isEmpty(attachments)) {
                sendHTMLMail(fromEmail, fromName, toEmails, ccEmails, bccEmails, subject, html);
            } else {
                HtmlEmail email = getBasicEmail(fromEmail, fromName, toEmails, ccEmails, bccEmails, subject, html);

                for (AttachmentSource attachment : attachments) {
                    email.attach(attachment.getAttachmentObj());
                }

                email.send();
            }
        } catch (EmailException e) {
            throw new MyCollabException(e);
        }
    }

    private boolean isValidate(String val) {
        return StringUtils.isNotBlank(val);
    }
}
