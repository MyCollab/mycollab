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

@BaseName("localization/project/follower")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum FollowerI18nEnum {
	DIALOG_WATCHERS_TITLE,
	LABEL_MY_FOLLOWING_TICKETS,
	BUTTON_BACK_TO_WORKBOARD,
	FOLLOWER_NAME,
	FOLLOWER_CREATE_DATE,
	FORM_PROJECT_NAME,
	FORM_SUMMARY,
	
	OPT_NUM_FOLLOWERS,
	
	BUTTON_FOLLOW,
	BUTTON_UNFOLLOW,
	
	SUB_INFO_WATCHERS,
}
