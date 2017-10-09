package com.mycollab.vaadin.ui.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
public class CountryViewField extends CustomField<String> {
    private String countryCode;

    public CountryViewField(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    protected Component initContent() {
        if (StringUtils.isNotBlank(countryCode)) {
            Locale obj = new Locale("", countryCode);
            return new ELabel(obj.getDisplayCountry(UserUIContext.getUserLocale()));
        }
        return new ELabel("");
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
