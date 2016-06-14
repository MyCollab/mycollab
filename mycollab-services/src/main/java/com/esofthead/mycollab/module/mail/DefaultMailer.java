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
import com.esofthead.mycollab.configuration.EmailConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
            for (int i = 0; i < toEmail.size(); i++) {
                if (isValidate(toEmail.get(i).getEmail()) && isValidate(toEmail.get(i).getName())) {
                    email.addTo(toEmail.get(i).getEmail(), toEmail.get(i).getName());
                } else {
                    LOG.error(String.format("Invalid to email input: %s---%s", toEmail.get(i).getEmail(), toEmail.get(i).getName()));
                }
            }

            if (CollectionUtils.isNotEmpty(ccEmail)) {
                for (int i = 0; i < ccEmail.size(); i++) {
                    if (isValidate(ccEmail.get(i).getEmail()) && isValidate(ccEmail.get(i).getName())) {
                        email.addCc(ccEmail.get(i).getEmail(), ccEmail.get(i).getName());
                    } else {
                        LOG.error(String.format("Invalid cc email input: %s---%s", ccEmail.get(i).getEmail(), ccEmail.get(i).getName()));
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(bccEmail)) {
                for (int i = 0; i < bccEmail.size(); i++) {
                    if (isValidate(bccEmail.get(i).getEmail()) && isValidate(bccEmail.get(i).getName())) {
                        email.addBcc(bccEmail.get(i).getEmail(), bccEmail.get(i).getName());
                    } else {
                        LOG.error(String.format("Invalid bcc email input: %s---%s", bccEmail.get(i).getEmail(), bccEmail.get(i).getName()));
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
    public void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmail, List<MailRecipientField> ccEmail,
                             List<MailRecipientField> bccEmail, String subject, String html) {
        try {
            HtmlEmail email = getBasicEmail(fromEmail, fromName, toEmail, ccEmail, bccEmail, subject, html);
            email.send();
        } catch (EmailException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmail,
                             List<MailRecipientField> ccEmail, List<MailRecipientField> bccEmail,
                             String subject, String html, List<? extends EmailAttachmentSource> attachments) {
        try {
            if (CollectionUtils.isEmpty(attachments)) {
                sendHTMLMail(fromEmail, fromName, toEmail, ccEmail, bccEmail, subject, html);
            } else {
                HtmlEmail email = getBasicEmail(fromEmail, fromName, toEmail, ccEmail, bccEmail, subject, html);

                for (EmailAttachmentSource attachment : attachments) {
                    email.attach(attachment.getAttachmentObj());
                }

                email.send();
            }
        } catch (EmailException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public void sendHTMLMail(String fromEmail, String fromName, List<SimpleUser> users, String subject, String html,
                             List<? extends EmailAttachmentSource> attachment) {
        List<MailRecipientField> lstRecipient = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            String mail = users.get(i).getEmail();
            String mailName = isValidate(users.get(i).getDisplayName()) ? mail : users.get(i).getDisplayName();
            lstRecipient.add(new MailRecipientField(mail, mailName));
        }

        this.sendHTMLMail(fromEmail, fromName, lstRecipient, null, null, subject, html, attachment);
    }

    private boolean isValidate(String val) {
        return StringUtils.isNotBlank(val);
    }
}
