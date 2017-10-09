package com.mycollab.mobile.ui;

import com.mycollab.module.crm.CrmDataTypeFactory;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class IndustryListSelect extends I18NValueListSelect {
    private static final long serialVersionUID = 1L;

    public IndustryListSelect() {
        setCaption(null);
        loadData(Arrays.asList(CrmDataTypeFactory.accountIndustryList));
    }
}
