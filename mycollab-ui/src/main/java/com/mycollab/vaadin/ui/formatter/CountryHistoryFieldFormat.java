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
package com.mycollab.vaadin.ui.formatter;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;

import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
public class CountryHistoryFieldFormat implements HistoryFieldFormat {
    @Override
    public String toString(String countryCode) {
        if (StringUtils.isNotBlank(countryCode)) {
            Locale obj = new Locale("", countryCode);
            return obj.getDisplayCountry(AppContext.getUserLocale());
        } else {
            return "";
        }
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        return toString(value);
    }
}
