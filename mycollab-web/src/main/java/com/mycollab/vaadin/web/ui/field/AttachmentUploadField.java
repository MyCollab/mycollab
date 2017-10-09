package com.mycollab.vaadin.web.ui.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.AttachmentDisplayComponent;
import com.mycollab.vaadin.web.ui.AttachmentPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class AttachmentUploadField extends CustomField {
    private static final long serialVersionUID = 1L;

    private ResourceService resourceService;
    private AttachmentPanel attachmentPanel;
    private String attachmentPath;

    public AttachmentUploadField() {
        this(null);
    }

    public AttachmentUploadField(String attachmentPath) {
        this.attachmentPath = attachmentPath;
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    public void saveContentsToRepo(String attachmentPath) {
        attachmentPanel.saveContentsToRepo(attachmentPath);
    }

    @Override
    protected Component initContent() {
        attachmentPanel = new AttachmentPanel();
        if (StringUtils.isNotBlank(attachmentPath)) {
            List<Content> attachments = resourceService.getContents(attachmentPath);
            if (CollectionUtils.isNotEmpty(attachments)) {
                AttachmentDisplayComponent attachmentDisplayComponent = new AttachmentDisplayComponent(attachments);
                return new MVerticalLayout(attachmentDisplayComponent, attachmentPanel);
            } else {
                return attachmentPanel;
            }
        } else {
            return attachmentPanel;
        }
    }
}
