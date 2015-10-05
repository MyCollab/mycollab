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
package com.esofthead.mycollab.common.domain;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ContentJcrDao;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleComment extends CommentWithBLOBs {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleComment.class);

    private String ownerAvatarId;
    private String ownerFullName;
    private List<Content> attachments;

    public String getOwnerFullName() {
        if (StringUtils.isBlank(ownerFullName)) {
            String displayName = getCreateduser();
            return StringUtils.extractNameFromEmail(displayName);
        }
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getOwnerAvatarId() {
        return ownerAvatarId;
    }

    public void setOwnerAvatarId(String ownerAvatarId) {
        this.ownerAvatarId = ownerAvatarId;
    }

    public List<Content> getAttachments() {
        try {
            if (attachments == null) {
                ContentJcrDao contentJcr = ApplicationContextUtil.getSpringBean(ContentJcrDao.class);
                String commentPath = AttachmentUtils.getCommentAttachmentPath(getType(), getSaccountid(), getExtratypeid(), getTypeid(), getId());
                attachments = contentJcr.getContents(commentPath);
            }
        } catch (Exception e) {
            LOG.error("Error while get attachments of comment " + getId()
                    + "---" + getSaccountid() + "---" + getExtratypeid()
                    + "---" + getTypeid(), e);
        }

        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        return attachments;
    }
}
