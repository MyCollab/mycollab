/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.vaadin.AppContext;
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
        super();
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();
        for (Currency currency : availableCurrencies) {
            this.addItem(currency.getCurrencyCode());
            this.setItemCaption(currency.getCurrencyCode(), String.format("%s (%s)", currency.getDisplayName
                    (AppContext.getUserLocale()), currency.getCurrencyCode()));
        }
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value == null) {
            Currency currency = AppContext.getDefaultCurrency();
            newDataSource.setValue(currency.getCurrencyCode());
        }
        super.setPropertyDataSource(newDataSource);
    }
}
