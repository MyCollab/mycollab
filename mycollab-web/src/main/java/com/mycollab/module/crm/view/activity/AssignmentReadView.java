package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.domain.SimpleCrmTask;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface AssignmentReadView extends IPreviewView<SimpleCrmTask> {

    HasPreviewFormHandlers<SimpleCrmTask> getPreviewFormHandlers();
}
