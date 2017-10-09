package com.mycollab.module.project.view.settings;

import com.mycollab.module.tracker.domain.Component;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ComponentAddView extends IFormAddView<Component> {

    HasEditFormHandlers<Component> getEditFormHandlers();

}
