package com.mycollab.module.crm.view.opportunity;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunitySalesStageComboBox extends I18nValueComboBox {

    public OpportunitySalesStageComboBox() {
        this.loadData(Arrays.asList(CrmDataTypeFactory.opportunitySalesStageList));
        this.setNullSelectionAllowed(false);
    }
}
