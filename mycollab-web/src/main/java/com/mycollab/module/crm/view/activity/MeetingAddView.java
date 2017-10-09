package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface MeetingAddView extends IFormAddView<MeetingWithBLOBs> {
    HasEditFormHandlers<MeetingWithBLOBs> getEditFormHandlers();
}
