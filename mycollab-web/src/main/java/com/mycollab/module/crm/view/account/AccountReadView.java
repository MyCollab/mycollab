package com.mycollab.module.crm.view.account;

import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.module.crm.domain.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface AccountReadView extends IPreviewView<SimpleAccount> {

    HasPreviewFormHandlers<SimpleAccount> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleContact> getRelatedContactHandlers();

    IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers();

    IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers();

    IRelatedListHandlers<SimpleCase> getRelatedCaseHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();
}
