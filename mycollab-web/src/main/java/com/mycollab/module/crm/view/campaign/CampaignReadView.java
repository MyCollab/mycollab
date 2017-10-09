package com.mycollab.module.crm.view.campaign;

import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.module.crm.domain.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface CampaignReadView extends IPreviewView<SimpleCampaign> {
    HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();

    IRelatedListHandlers<SimpleAccount> getRelatedAccountHandlers();

    IRelatedListHandlers<SimpleContact> getRelatedContactHandlers();

    IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers();
}
