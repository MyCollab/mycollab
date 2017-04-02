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
package com.mycollab.module.project.view.milestone;

import com.mycollab.common.TableViewField;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.fielddef.MilestoneTableFieldDef;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.vaadin.reporting.CustomizeReportOutputWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collection;

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
    protected Collection<TableViewField> getDefaultColumns() {
        return Arrays.asList(MilestoneTableFieldDef.milestoneName(), MilestoneTableFieldDef.startDate(),
                MilestoneTableFieldDef.endDate(), MilestoneTableFieldDef.status(), MilestoneTableFieldDef.assignee());
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(MilestoneTableFieldDef.milestoneName(), MilestoneTableFieldDef.startDate(),
                MilestoneTableFieldDef.endDate(), MilestoneTableFieldDef.status(), MilestoneTableFieldDef.assignee(),
                MilestoneTableFieldDef.billableHours(), MilestoneTableFieldDef.nonBillableHours());
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[]{"Milestone 1", UserUIContext.formatDate(new LocalDate().minusDays(30).toDate()), UserUIContext
                .formatDate(new LocalDate().plusDays(7).toDate()), UserUIContext.getMessage(MilestoneStatus.InProgress),
                "John Adam", "10", "2"};
    }
}
