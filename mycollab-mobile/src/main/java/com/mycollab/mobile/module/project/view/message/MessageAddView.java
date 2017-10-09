package com.mycollab.mobile.module.project.view.message;

import com.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public interface MessageAddView extends PageView {
    HasEditFormHandlers<SimpleMessage> getEditFormHandlers();

    void initView();

    ProjectFormAttachmentUploadField getUploadField();
}
