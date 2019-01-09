/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.Currency;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CurrencyComboBoxField extends ComboBox<Currency> implements Converter<Currency, String> {
    private static final long serialVersionUID = 1L;

    public CurrencyComboBoxField() {
        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();
        this.setItems(availableCurrencies);
        this.setItemCaptionGenerator((ItemCaptionGenerator<Currency>) currency -> String.format("%s (%s)", currency.getDisplayName
                (UserUIContext.getUserLocale()), currency.getCurrencyCode()));
    }

    @Override
    public Result<String> convertToModel(Currency value, ValueContext context) {
        return Result.ok(value.getCurrencyCode());
    }

    @Override
    public Currency convertToPresentation(String value, ValueContext context) {
        return (value == null) ? null : Currency.getInstance(value);
    }
}
