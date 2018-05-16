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
package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.*;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class OptionI18nEnum {

    public static StatusI18nEnum[] statuses = new StatusI18nEnum[]{Open, Overdue, Closed, Archived, Pending, InProgress, Unresolved, Verified, Resolved, ReOpen};

    @BaseName("common-generic-status")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum StatusI18nEnum {
        Open, Overdue, Closed, Archived, Pending, InProgress, Unresolved, Verified, Resolved, ReOpen
    }
}
