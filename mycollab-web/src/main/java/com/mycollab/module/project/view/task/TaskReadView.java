package com.mycollab.module.project.view.task;

import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface TaskReadView extends IPreviewView<SimpleTask> {
    HasPreviewFormHandlers<SimpleTask> getPreviewFormHandlers();
}
