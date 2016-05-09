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

@BaseName("localization/project-breadcrumb")
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum BreadcrumbI18nEnum {
    FRA_TASK_DASHBOARD,
    FRA_TIME_TRACKING,
    FRA_FILES,
    FRA_CALENDAR,
    FRA_STANDUP,
    FRA_STANDUP_FOR_DAY,
    FRA_MEMBERS,
    FRA_INVITE_MEMBERS,
    FRA_MEMBER_READ,
    FRA_MEMBER_EDIT,
    FRA_SETTING
}
