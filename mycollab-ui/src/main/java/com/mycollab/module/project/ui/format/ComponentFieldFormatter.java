/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class ComponentFieldFormatter extends FieldGroupFormatter {
    private static ComponentFieldFormatter _instance = new ComponentFieldFormatter();

    private ComponentFieldFormatter() {
        generateFieldDisplayHandler(Component.Field.name.name(), GenericI18Enum.FORM_NAME);
        generateFieldDisplayHandler(Component.Field.description.name(), GenericI18Enum.FORM_DESCRIPTION);
        generateFieldDisplayHandler(Component.Field.userlead.name(), ComponentI18nEnum.FORM_LEAD,
                new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler(Component.Field.status.name(), GenericI18Enum.FORM_STATUS,
                new I18nHistoryFieldFormat(StatusI18nEnum.class));
    }

    public static ComponentFieldFormatter instance() {
        return _instance;
    }
}
