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
package com.mycollab.vaadin.ui.field;

import com.mycollab.core.utils.CurrencyUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.util.Currency;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class CurrencyViewField extends CustomField<String> {
    private ELabel label;

    public CurrencyViewField() {
        label = new ELabel().withFullWidth().withStyleName(WebThemes.LABEL_WORD_WRAP);
    }

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    protected void doSetValue(String value) {
        if (StringUtils.isBlank(value)) {
            label.setValue("");
        } else {
            Currency currency = CurrencyUtils.getInstance(value);
            label.setValue(String.format("%s (%s)", currency.getDisplayName(UserUIContext.getUserLocale()), currency.getCurrencyCode()));
        }
    }

    @Override
    public String getValue() {
        return null;
    }
}
