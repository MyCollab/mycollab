package com.mycollab.module.project.view.user;

import com.mycollab.module.project.domain.Project;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectAddView extends IFormAddView<Project> {
    HasEditFormHandlers<Project> getEditFormHandlers();

    Project getItem();
}
