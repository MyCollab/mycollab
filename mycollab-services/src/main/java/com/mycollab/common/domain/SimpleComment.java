package com.mycollab.common.domain;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ContentJcrDao;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.spring.AppContextUtil;
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
                ContentJcrDao contentJcr = AppContextUtil.getSpringBean(ContentJcrDao.class);
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
