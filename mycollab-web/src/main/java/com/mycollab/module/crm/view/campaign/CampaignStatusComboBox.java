package com.mycollab.module.crm.view.campaign;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignStatusComboBox extends I18nValueComboBox {
    public CampaignStatusComboBox() {
        this.loadData(Arrays.asList(CrmDataTypeFactory.campaignStatusList));
    }
}
