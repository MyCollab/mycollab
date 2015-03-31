/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;

import com.esofthead.mycollab.core.format.DefaultDateFormat;
import com.esofthead.mycollab.core.format.IDateFormat;
import com.esofthead.mycollab.core.format.JpDateFormat;
import org.apache.commons.lang3.LocaleUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author MyCollab Ltd.
 * @since 4.3.0
 *
 */
public class LocaleHelper {
    private static Map<Locale, IDateFormat> dateFormats = new HashMap<>();

    static {
        dateFormats.put(Locale.JAPAN, new JpDateFormat());
        dateFormats.put(Locale.US, new DefaultDateFormat());
    }

    public static Locale toLocale(String language) {
        if (language == null) {
            return Locale.US;
        }

        Locale locale = LocaleUtils.toLocale(language);
        return (locale != null) ? locale : SiteConfiguration.getDefaultLocale();
    }

    public static IDateFormat getDateFormatInstance(Locale locale) {
        IDateFormat dateFormat = dateFormats.get(locale);
        if (dateFormat == null) {
            dateFormat = dateFormats.get(Locale.US);
        }
        return dateFormat;
    }
}
