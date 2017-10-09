package com.mycollab.module.project.view.message;

import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface MessageReadView extends IPreviewView<SimpleMessage> {

    HasPreviewFormHandlers<SimpleMessage> getPreviewFormHandlers();
}
