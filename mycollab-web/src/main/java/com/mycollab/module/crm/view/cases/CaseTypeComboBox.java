package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.mycollab.vaadin.web.ui.ValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public class CaseTypeComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public CaseTypeComboBox() {
        setCaption(null);
        this.loadData(Arrays.asList(CrmDataTypeFactory.casesType));
    }
}
