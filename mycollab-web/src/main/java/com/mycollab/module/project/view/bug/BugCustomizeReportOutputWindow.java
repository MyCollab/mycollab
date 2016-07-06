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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.TableViewField;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.reporting.CustomizeReportOutputWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class BugCustomizeReportOutputWindow extends CustomizeReportOutputWindow<BugSearchCriteria, SimpleBug> {
    public BugCustomizeReportOutputWindow(VariableInjector<BugSearchCriteria> variableInjector) {
        super(ProjectTypeConstants.BUG, AppContext.getMessage(BugI18nEnum.LIST), SimpleBug.class,
                AppContextUtil.getSpringBean(BugService.class), variableInjector);
    }

    @Override
    protected Collection<TableViewField> getDefaultColumns() {
        return Arrays.asList(BugTableFieldDef.summary(), BugTableFieldDef.environment(), BugTableFieldDef.priority(),
                BugTableFieldDef.severity(), BugTableFieldDef.status(), BugTableFieldDef.resolution(),
                BugTableFieldDef.logBy(), BugTableFieldDef.dueDate(), BugTableFieldDef.assignUser(),
                BugTableFieldDef.billableHours(), BugTableFieldDef.nonBillableHours());
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(BugTableFieldDef.summary(), BugTableFieldDef.environment(), BugTableFieldDef.priority(),
                BugTableFieldDef.severity(), BugTableFieldDef.status(), BugTableFieldDef.resolution(),
                BugTableFieldDef.logBy(), BugTableFieldDef.startDate(), BugTableFieldDef.endDate(), BugTableFieldDef.dueDate(),
                BugTableFieldDef.assignUser(), BugTableFieldDef.milestoneName(), BugTableFieldDef.billableHours(),
                BugTableFieldDef.nonBillableHours());
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[]{"Bug A", "Virtual Environment", OptionI18nEnum.BugPriority.Critical.name(),
                OptionI18nEnum.BugSeverity.Major.name(), OptionI18nEnum.BugStatus.Open.name(), OptionI18nEnum.BugResolution.None.name(),
                "John Adam", AppContext.formatDate(new LocalDate().minusDays(2).toDate()), AppContext.formatDate(new
                LocalDate().plusDays(1).toDate()), AppContext.formatDate(new LocalDate().plusDays(2).toDate()),
                "Will Smith", "Project Execution", "10", "2"};
    }
}
