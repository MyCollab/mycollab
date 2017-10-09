package com.mycollab.vaadin.ui;

import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;

import java.util.Currency;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CurrencyComboBoxField extends ComboBox {
    private static final long serialVersionUID = 1L;

    public CurrencyComboBoxField() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();
        for (Currency currency : availableCurrencies) {
            this.addItem(currency.getCurrencyCode());
            this.setItemCaption(currency.getCurrencyCode(), String.format("%s (%s)", currency.getDisplayName
                    (UserUIContext.getUserLocale()), currency.getCurrencyCode()));
        }
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value == null) {
            Currency currency = AppUI.getDefaultCurrency();
            newDataSource.setValue(currency.getCurrencyCode());
        }
        super.setPropertyDataSource(newDataSource);
    }
}
