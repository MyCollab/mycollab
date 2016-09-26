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
package com.mycollab.module.project.view.ticket;

import com.mycollab.common.TableViewField;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.fielddef.TicketTableFieldDef;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.reporting.CustomizeReportOutputWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class TicketCustomizeReportOutputWindow extends CustomizeReportOutputWindow<ProjectTicketSearchCriteria, ProjectTicket> {
    public TicketCustomizeReportOutputWindow(VariableInjector<ProjectTicketSearchCriteria> variableInjector) {
        super(ProjectTypeConstants.TICKET, UserUIContext.getMessage(TicketI18nEnum.LIST), ProjectTicket.class,
                AppContextUtil.getSpringBean(ProjectTicketService.class), variableInjector);
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[]{"Task A", "Description 1", UserUIContext.formatDate(new LocalDate().minusDays(2).toDate()),
                UserUIContext.formatDate(new LocalDate().plusDays(1).toDate()), UserUIContext.formatDate(new LocalDate().plusDays(1).toDate()),
                Priority.High.name(), "Will Smith", "Jonh Adam", "3", "1"};
    }

    @Override
    protected Collection<TableViewField> getDefaultColumns() {
        return Arrays.asList(TicketTableFieldDef.name(), TicketTableFieldDef.startdate(), TicketTableFieldDef.duedate(),
                TicketTableFieldDef.priority(), TicketTableFieldDef.assignee(),
                TicketTableFieldDef.billableHours(), TicketTableFieldDef.nonBillableHours());
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(TicketTableFieldDef.name(), TicketTableFieldDef.description(), TicketTableFieldDef.startdate(),
                TicketTableFieldDef.enddate(), TicketTableFieldDef.duedate(),
                TicketTableFieldDef.priority(), TicketTableFieldDef.logUser(),
                TicketTableFieldDef.assignee(), TicketTableFieldDef.billableHours(), TicketTableFieldDef.nonBillableHours());
    }
}
