package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectRoleReadView extends IPreviewView<SimpleProjectRole> {

    HasPreviewFormHandlers<SimpleProjectRole> getPreviewFormHandlers();
}
