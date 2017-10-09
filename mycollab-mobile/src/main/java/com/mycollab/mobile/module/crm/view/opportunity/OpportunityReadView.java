package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public interface OpportunityReadView extends IPreviewView<SimpleOpportunity> {

    HasPreviewFormHandlers<SimpleOpportunity> getPreviewFormHandlers();

}
