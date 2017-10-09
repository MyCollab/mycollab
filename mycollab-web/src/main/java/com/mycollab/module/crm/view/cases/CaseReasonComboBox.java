package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseReasonComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public CaseReasonComboBox() {
        setCaption(null);
        this.loadData(Arrays.asList(CrmDataTypeFactory.casesReason));
    }
}
