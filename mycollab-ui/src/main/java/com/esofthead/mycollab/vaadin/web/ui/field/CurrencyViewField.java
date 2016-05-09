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
package com.esofthead.mycollab.vaadin.web.ui.field;

import com.esofthead.mycollab.core.utils.CurrencyUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.util.Currency;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class CurrencyViewField extends CustomField<String> {
    private ELabel label;

    public CurrencyViewField(String value) {
        if (StringUtils.isBlank(value)) {
            label = new ELabel();
        } else {
            Currency currency = CurrencyUtils.getInstance(value);
            label = new ELabel(value).withWidth("100%").withStyleName("wordWrap").withWidthUndefined()
                    .withDescription(currency.getDisplayName(AppContext.getUserLocale()));
        }
    }

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
