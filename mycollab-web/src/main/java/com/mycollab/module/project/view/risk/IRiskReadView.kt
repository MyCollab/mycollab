package com.mycollab.module.project.view.risk

import com.mycollab.module.project.domain.SimpleRisk
import com.mycollab.vaadin.event.HasPreviewFormHandlers
import com.mycollab.vaadin.mvp.IPreviewView

interface IRiskReadView : IPreviewView<SimpleRisk> {
    fun getPreviewFormHandlers(): HasPreviewFormHandlers<SimpleRisk>
}