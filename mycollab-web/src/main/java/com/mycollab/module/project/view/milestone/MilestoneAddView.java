package com.mycollab.module.project.view.milestone;

import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.vaadin.mvp.IFormAddView;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface MilestoneAddView extends IFormAddView<SimpleMilestone> {
    AttachmentUploadField getAttachUploadField();
}
