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
package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class VersionFieldFormatter extends FieldGroupFormatter {
    private static VersionFieldFormatter _instance = new VersionFieldFormatter();

    private VersionFieldFormatter() {
        generateFieldDisplayHandler("name", GenericI18Enum.FORM_NAME);
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, new I18nHistoryFieldFormat(StatusI18nEnum.class));
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
        generateFieldDisplayHandler("duedate", GenericI18Enum.FORM_DUE_DATE, DATE_FIELD);
    }

    public static VersionFieldFormatter instance() {
        return _instance;
    }
}
