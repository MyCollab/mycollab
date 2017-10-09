package com.mycollab.module.crm.view.lead;

import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface LeadAddView extends IFormAddView<SimpleLead> {
    HasEditFormHandlers<SimpleLead> getEditFormHandlers();
}
