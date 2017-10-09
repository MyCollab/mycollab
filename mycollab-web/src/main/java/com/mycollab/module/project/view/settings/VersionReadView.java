package com.mycollab.module.project.view.settings;

import com.mycollab.module.tracker.domain.Version;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface VersionReadView extends IPreviewView<Version> {

    HasPreviewFormHandlers<Version> getPreviewFormHandlers();

}
