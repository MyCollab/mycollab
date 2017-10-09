package com.mycollab.vaadin.web.ui;

import com.mycollab.common.CountryValueFactory;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.ComboBox;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CountryComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public CountryComboBox() {
        String[] countryList = CountryValueFactory.countryList;
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        Arrays.stream(countryList).forEach(country -> {
            Locale obj = new Locale("", country);
            this.addItem(country);
            this.setItemCaption(country, obj.getDisplayCountry(UserUIContext.getUserLocale()));
        });
    }
}
