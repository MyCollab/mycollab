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
package com.mycollab.vaadin.ui.formatter;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;

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
            return obj.getDisplayCountry(UserUIContext.getUserLocale());
        } else {
            return "";
        }
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        return toString(value);
    }
}
