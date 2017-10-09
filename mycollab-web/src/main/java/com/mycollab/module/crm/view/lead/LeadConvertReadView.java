package com.mycollab.module.crm.view.lead;

import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public interface LeadConvertReadView extends IPreviewView<SimpleLead> {
	HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers();

	IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();

	IRelatedListHandlers<SimpleCampaign> getRelatedCampaignHandlers();

	void displayConvertLeadInfo(SimpleLead lead);
}
