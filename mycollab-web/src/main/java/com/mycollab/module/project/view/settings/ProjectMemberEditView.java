package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectMemberEditView extends IFormAddView<SimpleProjectMember> {

    HasEditFormHandlers<SimpleProjectMember> getEditFormHandlers();

}
