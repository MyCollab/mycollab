package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.domain.CrmTask;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface AssignmentAddView extends IFormAddView<CrmTask> {
    HasEditFormHandlers<CrmTask> getEditFormHandlers();
}
