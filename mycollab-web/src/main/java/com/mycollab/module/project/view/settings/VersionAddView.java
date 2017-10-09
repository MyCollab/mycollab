package com.mycollab.module.project.view.settings;

import com.mycollab.module.tracker.domain.Version;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface VersionAddView extends IFormAddView<Version> {

    HasEditFormHandlers<Version> getEditFormHandlers();

}
