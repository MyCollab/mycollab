/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class ComponentFieldFormatter extends FieldGroupFormatter {
    private static ComponentFieldFormatter _instance = new ComponentFieldFormatter();

    private ComponentFieldFormatter() {
        generateFieldDisplayHandler("componentname", ComponentI18nEnum.FORM_NAME);
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION);
        generateFieldDisplayHandler("userlead", ComponentI18nEnum.FORM_LEAD, new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("status", ComponentI18nEnum.FORM_STATUS,
                new I18nHistoryFieldFormat(OptionI18nEnum.StatusI18nEnum.class));
    }

    public static ComponentFieldFormatter instance() {
        return _instance;
    }
}
