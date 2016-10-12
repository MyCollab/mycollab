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
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class MilestoneFieldFormatter extends FieldGroupFormatter {
    private static MilestoneFieldFormatter _instance = new MilestoneFieldFormatter();

    private MilestoneFieldFormatter() {
        generateFieldDisplayHandler("name", GenericI18Enum.FORM_NAME);
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS,
                new I18nHistoryFieldFormat(OptionI18nEnum.MilestoneStatus.class));
        generateFieldDisplayHandler(Milestone.Field.assignuser.name(), GenericI18Enum.FORM_ASSIGNEE,
                new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("startdate", GenericI18Enum.FORM_START_DATE, DATE_FIELD);
        generateFieldDisplayHandler("enddate", GenericI18Enum.FORM_END_DATE, DATE_FIELD);
        generateFieldDisplayHandler(Milestone.Field.description.name(), GenericI18Enum.FORM_DESCRIPTION, TRIM_HTMLS);
    }

    public static MilestoneFieldFormatter instance() {
        return _instance;
    }
}
