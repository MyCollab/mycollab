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

@BaseName("localization/project/standup")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum StandupI18nEnum {
	VIEW_LIST_TITLE,
	CHOOSE_REPORT_DATE,
	BUTTON_ADD_REPORT_LABEL,
	
	STANDUP_LASTDAY,
	STANDUP_TODAY,
	STANDUP_ISSUE,
	STANDUP_ISSUE_SHORT,
	
	STANDUP_MEMBER_NOT_REPORT,
	STANDUP_NO_ITEM,
	
	HINT1_MSG,
	HINT2_MG,
	
	FORM_EDIT_TITLE,
}
