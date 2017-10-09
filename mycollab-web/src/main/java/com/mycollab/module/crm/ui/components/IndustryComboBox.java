package com.mycollab.module.crm.ui.components;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class IndustryComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public IndustryComboBox() {
        setCaption(null);
        loadData(Arrays.asList(CrmDataTypeFactory.accountIndustryList));
    }
}
