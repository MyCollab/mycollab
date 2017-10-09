package com.mycollab.mobile.ui;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.ListSelect;

import java.util.Currency;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CurrencyComboBoxField extends ListSelect {
    private static final long serialVersionUID = 1L;

    public CurrencyComboBoxField() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setRows(1);

        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();
        for (Currency currency : availableCurrencies) {
            this.addItem(currency.getCurrencyCode());
            this.setItemCaption(currency.getCurrencyCode(), String.format("%s (%s)", currency.getDisplayName
                    (UserUIContext.getUserLocale()), currency.getCurrencyCode()));
        }
    }
}
