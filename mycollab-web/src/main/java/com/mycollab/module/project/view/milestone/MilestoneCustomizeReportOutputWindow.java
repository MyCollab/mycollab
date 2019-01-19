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
package com.mycollab.module.project.view.milestone;

import com.google.common.collect.Sets;
import com.mycollab.common.TableViewField;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.fielddef.MilestoneTableFieldDef;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.reporting.CustomizeReportOutputWindow;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class MilestoneCustomizeReportOutputWindow extends CustomizeReportOutputWindow<MilestoneSearchCriteria, SimpleMilestone> {
    public MilestoneCustomizeReportOutputWindow(VariableInjector<MilestoneSearchCriteria> variableInjector) {
        super(ProjectTypeConstants.MILESTONE, UserUIContext.getMessage(MilestoneI18nEnum.LIST), SimpleMilestone.class,
                AppContextUtil.getSpringBean(MilestoneService.class), variableInjector);
    }

    @Override
    protected Set<TableViewField> getDefaultColumns() {
        return Sets.newHashSet(MilestoneTableFieldDef.milestoneName, MilestoneTableFieldDef.startDate,
                MilestoneTableFieldDef.endDate, MilestoneTableFieldDef.status, MilestoneTableFieldDef.assignee);
    }

    @Override
    protected Set<TableViewField> getAvailableColumns() {
        return Sets.newHashSet(MilestoneTableFieldDef.milestoneName, MilestoneTableFieldDef.startDate,
                MilestoneTableFieldDef.endDate, MilestoneTableFieldDef.status, MilestoneTableFieldDef.assignee,
                MilestoneTableFieldDef.billableHours, MilestoneTableFieldDef.nonBillableHours);
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[]{"Milestone 1", UserUIContext.formatDate(LocalDateTime.now().minusDays(30)), UserUIContext
                .formatDate(LocalDateTime.now().plusDays(7)), UserUIContext.getMessage(MilestoneStatus.InProgress),
                "John Adam", "10", "2"};
    }
}
