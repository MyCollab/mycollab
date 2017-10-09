package com.mycollab.module.crm.view.opportunity;

import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface OpportunityAddView extends IFormAddView<SimpleOpportunity> {
    HasEditFormHandlers<SimpleOpportunity> getEditFormHandlers();

}
