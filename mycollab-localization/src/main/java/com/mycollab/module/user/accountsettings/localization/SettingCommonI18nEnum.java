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
package com.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("account-setting")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum SettingCommonI18nEnum {
    BUTTON_CHANGE_LOGO,
    BUTTON_RESET_DEFAULT,

    FORM_TOP_MENU,
    FORM_NORMAL_TAB,
    FORM_NORMAL_MENU,
    FORM_NORMAL_MENU_TEXT,
    FORM_NORMAL_TAB_TEXT
}
