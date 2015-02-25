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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.ui.format.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;
import com.esofthead.mycollab.utils.FieldGroupFormatter.I18nHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneHistoryLogList extends HistoryLogComponent {
    private static final long serialVersionUID = 1L;

    public static final FieldGroupFormatter milestoneFormatter;

    static {
        milestoneFormatter = new FieldGroupFormatter();

        milestoneFormatter.generateFieldDisplayHandler("name",
                MilestoneI18nEnum.FORM_NAME_FIELD);
        milestoneFormatter.generateFieldDisplayHandler("status",
                MilestoneI18nEnum.FORM_STATUS_FIELD,
                new I18nHistoryFieldFormat(MilestoneStatus.class));
        milestoneFormatter.generateFieldDisplayHandler("owner",
                GenericI18Enum.FORM_ASSIGNEE,
                new ProjectMemberHistoryFieldFormat());
        milestoneFormatter.generateFieldDisplayHandler("startdate",
                MilestoneI18nEnum.FORM_START_DATE_FIELD,
                FieldGroupFormatter.DATE_FIELD);
        milestoneFormatter.generateFieldDisplayHandler("enddate",
                MilestoneI18nEnum.FORM_END_DATE_FIELD,
                FieldGroupFormatter.DATE_FIELD);
        milestoneFormatter.generateFieldDisplayHandler(Milestone.Field.description.name(), GenericI18Enum.FORM_DESCRIPTION);
    }

    public MilestoneHistoryLogList(String module, String type) {
        super(module, type);
    }

    @Override
    protected FieldGroupFormatter buildFormatter() {
        return milestoneFormatter;
    }

}
