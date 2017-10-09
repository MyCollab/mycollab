package com.mycollab.mobile.module.project.view.task

import com.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.vaadin.mvp.IFormAddView

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
interface TaskAddView : IFormAddView<SimpleTask> {
    val attachUploadField: ProjectFormAttachmentUploadField
}
