package com.mycollab.module.crm.view.opportunity;

import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface OpportunityReadView extends IPreviewView<SimpleOpportunity> {

    HasPreviewFormHandlers<SimpleOpportunity> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();

    IRelatedListHandlers<SimpleContactOpportunityRel> getRelatedContactHandlers();

    IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers();
}
