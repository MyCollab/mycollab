/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        super();
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
