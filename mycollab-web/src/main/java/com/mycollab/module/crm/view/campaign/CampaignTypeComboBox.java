package com.mycollab.module.crm.view.campaign;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public class CampaignTypeComboBox extends I18nValueComboBox {
    public CampaignTypeComboBox() {
        this.loadData(Arrays.asList(CrmDataTypeFactory.campaignTypeList));
    }
}
