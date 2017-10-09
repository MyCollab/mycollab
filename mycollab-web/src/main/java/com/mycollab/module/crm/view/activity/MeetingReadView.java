package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.domain.SimpleMeeting;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface MeetingReadView extends IPreviewView<SimpleMeeting> {
    HasPreviewFormHandlers<SimpleMeeting> getPreviewFormHandlers();

}
