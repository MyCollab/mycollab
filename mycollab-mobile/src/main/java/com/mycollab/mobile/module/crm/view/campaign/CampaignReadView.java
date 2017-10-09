package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public interface CampaignReadView extends IPreviewView<SimpleCampaign> {
    HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers();
}
