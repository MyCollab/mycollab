package com.mycollab.module.project.ui.components;

import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.web.ui.AttachmentDisplayComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectAttachmentDisplayComponentFactory {
    public static Component getAttachmentDisplayComponent(int projectId, String type, int typeId) {
        ResourceService resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        List<Content> attachments = resourceService.getContents(AttachmentUtils.
                getProjectEntityAttachmentPath(AppUI.getAccountId(), projectId, type, "" + typeId));
        if (CollectionUtils.isNotEmpty(attachments)) {
            return new AttachmentDisplayComponent(attachments);
        } else {
            return new VerticalLayout();
        }
    }
}
