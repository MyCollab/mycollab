package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public interface BugAddView extends IFormAddView<SimpleBug> {

    HasEditFormHandlers<SimpleBug> getEditFormHandlers();

    ProjectFormAttachmentUploadField getAttachUploadField();
}
