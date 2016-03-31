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
package com.esofthead.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyCollab Ltd.
 * @since 4.1.1
 */
@BaseName("localization/common-file")
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum FileI18nEnum {
    EXCEL,
    PDF,
    CSV,
    IMPORT_FILE,
    EXPORT_FILE,
    ATTACH_FILES,
    NOT_ATTACH_FILE_WARNING,
    IMPORT_FILE_SUCCESS,
    CHOOSE_SUPPORT_FILE_TYPES_WARNING,
    OPT_MY_DOCUMENTS
}
