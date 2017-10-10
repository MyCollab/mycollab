/**
 * mycollab-localization - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-tag")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum TagI18nEnum {
    ACTION_ADD,
    ACTION_DELETE,

    OPT_TAG_CLOUD,
    OPT_NO_TAG_EXISTED,
    OPT_ENTER_TAG_NAME,

    ERROR_TAG_NAME_HAS_MORE_2_CHARACTERS
}
