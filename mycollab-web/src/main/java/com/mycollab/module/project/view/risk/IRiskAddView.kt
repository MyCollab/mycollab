package com.mycollab.module.project.view.risk

import com.mycollab.module.project.domain.SimpleRisk
import com.mycollab.vaadin.mvp.IFormAddView
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField

/**
 * @author MyCollab Ltd
 * @since 7.0
 */
interface IRiskAddView : IFormAddView<SimpleRisk> {

    fun getAttachUploadField(): AttachmentUploadField
}