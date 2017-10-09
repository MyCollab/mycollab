package com.mycollab.mobile.module.project.view.settings;

import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public interface ProjectMemberReadView extends IPreviewView<SimpleProjectMember> {

	HasPreviewFormHandlers<SimpleProjectMember> getPreviewFormHandlers();
}
