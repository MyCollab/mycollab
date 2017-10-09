package com.mycollab.module.project.view.settings;

import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ComponentReadView extends IPreviewView<SimpleComponent> {

    HasPreviewFormHandlers<SimpleComponent> getPreviewFormHandlers();
}
