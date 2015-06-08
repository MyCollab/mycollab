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

import java.util.List;

import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.common.service.CurrencyService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class CurrencyComboBoxField extends CustomField<Integer> {
    private static final long serialVersionUID = 1L;

    private ComboBox currencyBox;

    public CurrencyComboBoxField() {
        super();

        currencyBox = new ComboBox();
        currencyBox.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

        CurrencyService currencyService = ApplicationContextUtil
                .getSpringBean(CurrencyService.class);
        List<Currency> currencyList = currencyService.getCurrencies();
        for (Currency currency : currencyList) {
            currencyBox.addItem(currency.getId());
            currencyBox.setItemCaption(currency.getId(),
                    String.format("%s (%s)", currency.getShortname(), currency.getSymbol()));
        }
    }

    @Override
    protected Component initContent() {
        return currencyBox;
    }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof Integer) {
            currencyBox.setValue(value);
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void commit() throws SourceException, InvalidValueException {
        Integer value = (Integer) currencyBox.getValue();
        this.setInternalValue(value);
        super.commit();
    }
}
