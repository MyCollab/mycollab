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
package com.esofthead.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/account-setting")
@LocaleData(value = { @Locale("en-US"), @Locale("ja-JP") }, defaultCharset = "UTF-8")
public enum SettingCommonI18nEnum {
	BUTTON_CHANGE_LOGO,
	BUTTON_RESET_DEFAULT,

	FORM_TOP_MENU,
	FORM_NORMAL_TAB,
	FORM_NORMAL_MENU,
	FORM_NORMAL_MENU_TEXT,
	FORM_NORMAL_TAB_TEXT
}
