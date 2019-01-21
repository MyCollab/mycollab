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

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
public class CountryViewField extends CustomField<String> {
    private Label label;

    public CountryViewField() {
        label = new Label();
    }

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    protected void doSetValue(String countryCode) {
        if (StringUtils.isNotBlank(countryCode)) {
            Locale obj = new Locale("", countryCode);
            label.setValue(obj.getDisplayCountry(UserUIContext.getUserLocale()));
        } else label.setValue("");
    }

    @Override
    public String getValue() {
        return null;
    }
}
