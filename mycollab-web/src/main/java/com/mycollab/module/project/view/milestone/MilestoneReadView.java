package com.mycollab.module.project.view.milestone;

import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface MilestoneReadView extends IPreviewView<SimpleMilestone> {

    HasPreviewFormHandlers<SimpleMilestone> getPreviewFormHandlers();

}
