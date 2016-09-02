/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.CountryValueFactory;
import com.mycollab.vaadin.AppContext;
import com.vaadin.ui.ComboBox;

import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CountryComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public CountryComboBox() {
        String[] countryList = CountryValueFactory.getCountryList();
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        for (String country : countryList) {
            Locale obj = new Locale("", country);
            this.addItem(country);
            this.setItemCaption(country, obj.getDisplayCountry(AppContext.getUserLocale()));
        }
    }
}
