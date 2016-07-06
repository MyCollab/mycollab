/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-security")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum SecurityI18nEnum {
    NO_ACCESS, READONLY, READ_WRITE, ACCESS, YES, NO, UNDEFINE, ACCESS_PERMISSION_HELP, BOOLEAN_PERMISSION_HELP;

    public Enum desc() {
        if (this == NO_ACCESS || this == READONLY || this == READ_WRITE || this == ACCESS) {
            return ACCESS_PERMISSION_HELP;
        } else if (this == YES || this == NO) {
            return BOOLEAN_PERMISSION_HELP;
        }
        return UNDEFINE;
    }
}
