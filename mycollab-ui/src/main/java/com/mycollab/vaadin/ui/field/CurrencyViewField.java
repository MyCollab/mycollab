/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui.field;

import com.mycollab.core.utils.CurrencyUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
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
            label = new ELabel("");
        } else {
            Currency currency = CurrencyUtils.getInstance(value);
            label = new ELabel(String.format("%s (%s)", currency.getDisplayName(UserUIContext.getUserLocale()), currency.getCurrencyCode()))
                    .withFullWidth().withStyleName(UIConstants.LABEL_WORD_WRAP);
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
