package com.mycollab.module.project.view.bug;

import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface BugReadView extends IPreviewView<SimpleBug> {

    HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers();
}
