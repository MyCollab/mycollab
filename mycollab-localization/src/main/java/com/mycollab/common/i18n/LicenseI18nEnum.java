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

@BaseName("common-license")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum LicenseI18nEnum {
    ACTION_BUY_LICENSE,
    ACTION_ENTER_LICENSE,
    ACTION_CHANGE_LICENSE,
    OPT_LICENSE_EXPIRE_DATE,
    OPT_LICENSE_EXPIRE_SOON_DATE,
    OPT_LICENSE_VALID_TO_DATE,
    OPT_ACTIVATION_CODE,
    OPT_BROWSE_LICENSE_HELP,
    OPT_LICENSE_ACTIVATED,
    OPT_TRIAL_THE_PRO_EDITION,
    OPT_BUY_LICENSE,
    FORM_ORGANIZATION,
    FORM_ISSUE_DATE,
    FORM_EXPIRE_DATE,
    FORM_MAX_USERS,
    EXPIRE_NOTIFICATION,
    TRIAL_NOTIFICATION,
    ERROR_LICENSE_INVALID,
    ERROR_LICENSE_FILE_INVALID,
    FEATURE_NOT_AVAILABLE,
}
