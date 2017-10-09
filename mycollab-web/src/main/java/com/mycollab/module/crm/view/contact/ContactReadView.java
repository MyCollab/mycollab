package com.mycollab.module.crm.view.contact;

import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface ContactReadView extends IPreviewView<SimpleContact> {

    HasPreviewFormHandlers<SimpleContact> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();

    IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers();
}
