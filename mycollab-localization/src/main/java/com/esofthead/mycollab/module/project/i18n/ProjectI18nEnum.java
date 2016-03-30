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
package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project")
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum ProjectI18nEnum {
    VIEW_NEW_TITLE,

    FORM_HOME_PAGE,
    FORM_NAME,
    FORM_ACCOUNT_NAME,
    FORM_SHORT_NAME,
    FORM_STATUS,
    FORM_START_DATE,
    FORM_END_DATE,
    FORM_BILLING_RATE,
    FORM_OVERTIME_BILLING_RATE,
    FORM_CURRENCY,
    FORM_TARGET_BUDGET,
    FORM_ACTUAL_BUDGET,
    FORM_LEADER,

    SECTION_PROJECT_INFO,
    SECTION_FINANCE_SCHEDULE,
    SECTION_DESCRIPTION
}
