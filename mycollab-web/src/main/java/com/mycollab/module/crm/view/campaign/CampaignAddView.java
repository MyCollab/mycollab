package com.mycollab.module.crm.view.campaign;

import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface CampaignAddView extends IFormAddView<SimpleCampaign> {

    HasEditFormHandlers<SimpleCampaign> getEditFormHandlers();

}
