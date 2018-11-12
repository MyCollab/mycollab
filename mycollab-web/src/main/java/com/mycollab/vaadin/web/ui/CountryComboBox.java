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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.CountryValueFactory;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CountryComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public CountryComboBox() {
        String[] countries = CountryValueFactory.countries;
        this.setItems(countries);
        this.setItemCaptionGenerator((ItemCaptionGenerator<String>) country -> {
            Locale obj = new Locale("", country);
            return obj.getDisplayCountry(UserUIContext.getUserLocale());
        });
    }
}
