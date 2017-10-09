package com.mycollab.module.crm.view.lead;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class LeadStatusComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    LeadStatusComboBox() {
        super(false, CrmDataTypeFactory.leadStatusList);
    }
}
