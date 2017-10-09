package com.mycollab.module.project.view.task;

import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface TaskAddView extends IFormAddView<SimpleTask> {

    HasEditFormHandlers<SimpleTask> getEditFormHandlers();

    AttachmentUploadField getAttachUploadField();

    List<String> getFollowers();
}
