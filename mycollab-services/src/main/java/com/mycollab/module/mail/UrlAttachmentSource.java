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

import org.apache.commons.mail.EmailAttachment;

import java.net.URL;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
public class UrlAttachmentSource implements AttachmentSource {
    private String name;
    private URL url;

    public UrlAttachmentSource(String name, URL url) {
        this.url = url;
        this.name = name;
    }

    @Override
    public EmailAttachment getAttachmentObj() {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setURL(url);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setName(name);
        return attachment;
    }
}
