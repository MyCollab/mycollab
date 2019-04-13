/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.form.view.LayoutType;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.project.domain.Component;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class ComponentDefaultFormLayoutFactory {

    private static DynaSection mainSection() {
        DynaSection mainSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).build();

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Component.Field.name)
                .displayName(GenericI18Enum.FORM_NAME)
                .required(true).mandatory(true).fieldIndex(0).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Component.Field.description)
                .displayName(GenericI18Enum.FORM_DESCRIPTION)
                .fieldIndex(1).build());

        mainSection.fields(new TextDynaFieldBuilder().fieldName(Component.Field.userlead)
                .displayName(ComponentI18nEnum.FORM_LEAD)
                .fieldIndex(2).build());
        return mainSection;
    }

    private static DynaSection assignmentSection() {
        DynaSection assignmentSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).header(ProjectCommonI18nEnum.OPT_ASSIGNMENT_LIST).build();
        assignmentSection.fields(new TextDynaFieldBuilder().fieldName("section-assignments")
                .fieldIndex(6).colSpan(true).build());
        return assignmentSection;
    }

    public static DynaForm getAddForm() {
        return new DynaForm(mainSection());
    }

    public static DynaForm getReadForm() {
        return new DynaForm(mainSection(), assignmentSection());
    }
}
